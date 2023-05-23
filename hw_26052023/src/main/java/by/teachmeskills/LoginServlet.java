package by.teachmeskills;

import by.teachmeskills.exceptions.BadConnectionException;
import by.teachmeskills.utils.DBCrudUtils;
import by.teachmeskills.utils.DBUtils;
import by.teachmeskills.utils.HashUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    //To establish connection with DB once instead of doing that each time we make POST-request
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DBCrudUtils.setConnection(DBUtils::getConnection);
        } catch (BadConnectionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!((boolean) request.getSession().getAttribute("loggedIn"))) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
            requestDispatcher.forward(request, response);
        } else {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/page.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        User user = new User(request.getParameter("login"), HashUtils.getHash(request.getParameter("password")));
        RequestDispatcher requestDispatcher;
        try {
            if (DBCrudUtils.isUserPresent(user)) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("loggedIn", true);
                requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/page.jsp");
                requestDispatcher.forward(request, response);
            } else {
                requestDispatcher = request.getRequestDispatcher("/index.jsp");
                request.setAttribute("result", "Неверный логин или пароль");
                requestDispatcher.forward(request, response);
            }
        } catch (BadConnectionException e) {
            System.out.println("Can't login due to bad connection");
        }
    }

    @Override
    public void destroy() {
        try {
            DBCrudUtils.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.destroy();
    }
}