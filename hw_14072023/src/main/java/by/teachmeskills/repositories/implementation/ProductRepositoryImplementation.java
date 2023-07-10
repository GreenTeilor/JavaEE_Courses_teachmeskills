package by.teachmeskills.repositories.implementation;

import by.teachmeskills.entities.Product;
import by.teachmeskills.exceptions.BadConnectionException;
import by.teachmeskills.repositories.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProductRepositoryImplementation implements ProductRepository {
    private final static String GET_CATEGORY_PRODUCTS_QUERY = "SELECT * FROM products WHERE category = ?";
    private final static String GET_PRODUCT_BY_ID_QUERY = "SELECT * FROM products WHERE id = ?";

    private final static String GET_PRODUCTS_QUERY = "SELECT * FROM products";
    private final static String ADD_PRODUCT_QUERY = "INSERT INTO products (name, description, imagePath, category, price) VALUES (?, ?, ?, ?, ?)";
    private final static String DELETE_PRODUCT_BY_ID_QUERY = "DELETE FROM products WHERE id = ?";
    private final static String SEARCH_PRODUCTS_BY_NAME_QUERY = "SELECT * FROM products WHERE name LIKE ? ORDER BY name ASC";
    private final static String SEARCH_PRODUCTS_BY_DESCRIPTION_QUERY = "SELECT * FROM products WHERE description LIKE ? ORDER BY name ASC";

    @Override
    public List<Product> getCategoryProducts(String category) throws BadConnectionException {
        List<Product> result = new ArrayList<>();
        Connection connection = pool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_PRODUCTS_QUERY)) {
            statement.setString(1, category);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(Product.builder().id(set.getInt("id")).name(set.getString("name")).description(set.getString("description")).
                        description(set.getString("description")).imagePath(set.getString("imagePath")).category(set.getString("category")).
                        price(set.getBigDecimal("price")).build());
            }
            set.close();
            return result;
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query GET_PRODUCTS_IN_CATEGORY_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public Product getProductById(int id) throws BadConnectionException {
        Connection connection = pool.getConnection();
        Product product = null;
        try (PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_ID_QUERY)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                product = Product.builder().id(set.getInt("id")).name(set.getString("name")).description(set.getString("description")).
                        description(set.getString("description")).imagePath(set.getString("imagePath")).category(set.getString("category")).
                        price(set.getBigDecimal("price")).build();
            }
            set.close();
            return product;
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query GET_PRODUCT_BY_ID_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public List<Product> findProducts(String keyWords) throws BadConnectionException {
        Connection connection = pool.getConnection();
        List<Product> foundByName = new LinkedList<>();
        List<Product> foundByDescription = new LinkedList<>();
        try (PreparedStatement searchNameStatement = connection.prepareStatement(SEARCH_PRODUCTS_BY_NAME_QUERY);
             PreparedStatement searchDescriptionStatement = connection.prepareStatement(SEARCH_PRODUCTS_BY_DESCRIPTION_QUERY)) {
            keyWords = "%" + keyWords.trim() + "%";
            searchNameStatement.setString(1, keyWords);
            searchDescriptionStatement.setString(1, keyWords);
            ResultSet byNameSet = searchNameStatement.executeQuery();
            ResultSet byDescriptionSet = searchDescriptionStatement.executeQuery();
            while (byNameSet.next()) {
                foundByName.add(Product.builder().id(byNameSet.getInt("id")).name(byNameSet.getString("name")).
                        description(byNameSet.getString("description")).imagePath(byNameSet.getString("imagePath")).
                        category(byNameSet.getString("category")).price(byNameSet.getBigDecimal("price")).build());
            }
            while (byDescriptionSet.next()) {
                foundByDescription.add(Product.builder().id(byDescriptionSet.getInt("id")).name(byDescriptionSet.getString("name")).
                        description(byDescriptionSet.getString("description")).imagePath(byDescriptionSet.getString("imagePath")).
                        category(byDescriptionSet.getString("category")).price(byDescriptionSet.getBigDecimal("price")).build());
            }
            Set<Product> result = new LinkedHashSet<>(foundByName);
            result.addAll(foundByDescription);
            byNameSet.close();
            byDescriptionSet.close();
            return result.stream().toList();
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query SEARCH_PRODUCTS_BY_NAME_QUERY || SEARCH_PRODUCTS_BY_DESCRIPTION_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public Product create(Product product) throws BadConnectionException {
        Connection connection = pool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT_QUERY)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getImagePath());
            statement.setString(4, product.getCategory());
            statement.setBigDecimal(5, product.getPrice());
            statement.execute();
            ResultSet autoGenerated = statement.getGeneratedKeys();
            if (autoGenerated.next()) {
                product.setId(autoGenerated.getInt(1));
            }
            autoGenerated.close();
            return product;
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query ADD_PRODUCT_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public List<Product> read() throws BadConnectionException {
        List<Product> result = new ArrayList<>();
        Connection connection = pool.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(GET_PRODUCTS_QUERY);
            while (set.next()) {
                result.add(Product.builder().id(set.getInt("id")).name(set.getString("name")).description(set.getString("description")).
                        description(set.getString("description")).imagePath(set.getString("imagePath")).category(set.getString("category")).
                        price(set.getBigDecimal("price")).build());
            }
            set.close();
            return result;
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query GET_PRODUCTS_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public Product update(Product product) throws BadConnectionException {
        return getProductById(product.getId());
    }

    @Override
    public void delete(int id) throws BadConnectionException {
        Connection connection = pool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID_QUERY)) {
            statement.setString(1, String.valueOf(id));
            statement.execute();
        } catch (SQLException e) {
            throw new BadConnectionException("Unable to execute query DELETE_CATEGORY_BY_ID_QUERY");
        } finally {
            pool.returnConnection(connection);
        }
    }
}
