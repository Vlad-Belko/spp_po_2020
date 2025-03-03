package FacultyCompany.Persistence.Repositories;

import FacultyCompany.Entities.Subject;
import FacultyCompany.Persistence.Interfaces.IBaseRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SubjectRepository implements IBaseRepository<Subject> {

    private final Connection connection;

    public SubjectRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Subject Add(Subject entity) throws SQLException {
        var query =
                "INSERT INTO public.subjects(" +
                        " subjectname)" +
                        " VALUES (?)";

        var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getSubjectName());


        statement.execute();

        var generatedKeys = statement.getGeneratedKeys();
        generatedKeys.next();
        entity.setId(generatedKeys.getInt(1));

        return entity;
    }

    @Override
    public void Update(Subject entity) throws SQLException {
        var query =
                "UPDATE public.subjects" +
                        " SET subjectname = ?" +
                        " WHERE id = ?";

        var statement = connection.prepareStatement(query);
        statement.setString(1, entity.getSubjectName());
        statement.setInt(2, entity.getId());

        statement.executeUpdate();
    }

    @Override
    public void Delete(Subject entity) throws SQLException {
        var query = "DELETE FROM public.subjects" +
                " WHERE id=?";
        var statement = connection.prepareStatement(query);
        statement.setInt(1, entity.getId());

        statement.executeUpdate();
    }

    @Override
    public Subject GetByIdOrNull(int id) throws SQLException {
        var query =
                "SELECT * FROM public.subjects" +
                        " WHERE Id = ? " +
                        "Order by id";

        var statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        var reader = statement.executeQuery();
        if(reader.next())
        {
            var result = new Subject();
            result.setId(reader.getInt("id"));
            result.setSubjectName(reader.getString("subjectname"));
            return result;
        }

        return null;
    }

    @Override
    public ArrayList<Subject> GetAll() throws SQLException {
        var query =
                "SELECT * FROM public.subjects Order by id";

        var statement = connection.prepareStatement(query);

        var reader = statement.executeQuery();
        var result = new ArrayList<Subject>();
        while (reader.next())
        {
            var subject = new Subject();
            subject.setId(reader.getInt("id"));
            subject.setSubjectName(reader.getString("subjectname"));

            result.add(subject);
        }

        return result;
    }
}
