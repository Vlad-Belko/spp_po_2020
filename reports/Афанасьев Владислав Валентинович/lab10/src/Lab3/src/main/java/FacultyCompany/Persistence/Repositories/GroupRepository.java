package FacultyCompany.Persistence.Repositories;

import FacultyCompany.Entities.Group;
import FacultyCompany.Persistence.Interfaces.IBaseRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GroupRepository implements IBaseRepository<Group> {
    private final Connection connection;

    public GroupRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Group Add(Group entity) throws SQLException {
        var query =
                "INSERT INTO public.groups(" +
                        " groupname)" +
                        " VALUES (?)";

        var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getGroupname());

        statement.execute();

        var generatedKeys = statement.getGeneratedKeys();
        generatedKeys.next();
        entity.setId(generatedKeys.getInt(1));

        return entity;
    }

    @Override
    public void Update(Group entity) throws SQLException {
        var query =
                "UPDATE public.groups" +
                        " SET groupname = ?" +
                        " WHERE id = ?";

        var statement = connection.prepareStatement(query);
        statement.setString(1, entity.getGroupname());
        statement.setInt(2, entity.getId());

        statement.executeUpdate();
    }

    @Override
    public void Delete(Group entity) throws SQLException {
        var query = "DELETE FROM public.groups" +
                " WHERE id=?";
        var statement = connection.prepareStatement(query);
        statement.setInt(1, entity.getId());

        statement.executeUpdate();
    }

    @Override
    public Group GetByIdOrNull(int id) throws SQLException {
        var query =
                "SELECT * FROM public.groups" +
                        " WHERE Id = ?";

        var statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        var reader = statement.executeQuery();
        if(reader.next())
        {
            var result = new Group();
            result.setId(reader.getInt("id"));
            result.setGroupname(reader.getString("groupname"));
            return result;
        }

        return null;
    }

    @Override
    public ArrayList<Group> GetAll() throws SQLException {
        var query =
                "SELECT * FROM public.groups Order by id";

        var statement = connection.prepareStatement(query);

        var reader = statement.executeQuery();
        var result = new ArrayList<Group>();
        while (reader.next())
        {
            var group = new Group();
            group.setId(reader.getInt("id"));
            group.setGroupname(reader.getString("groupname"));

            result.add(group);
        }

        return result;
    }
}
