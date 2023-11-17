package org.csu.mypetstore.web.servlets;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.persistence.Impl.ShoppingCartDAOImpl;
import org.csu.mypetstore.service.LogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RemoveItemFromCartServlet extends HttpServlet {

    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";
    private static final String ERROR = "/WEB-INF/jsp/common/Error.jsp";

    private ShoppingCartDAOImpl shoppingCartDAO;

    public RemoveItemFromCartServlet() {
        this.shoppingCartDAO = new ShoppingCartDAOImpl(); // 确保这里正确创建了一个ShoppingCartDAOImpl实例
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workingItemId = request.getParameter("workingItemId");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");

        if (cart != null && cart.containsItemId(workingItemId)) {
            try {
                if (account != null) {
                    // 删除数据库中的购物车项
                    shoppingCartDAO.removeItemFromCart(account.getUsername(), workingItemId);
                    // 日志记录
                    logItemRemoval(account.getUsername(), workingItemId);
                }
                // 从会话的购物车中移除商品
                cart.removeItemById(workingItemId);
                request.getRequestDispatcher(VIEW_CART).forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("message", "Error removing item from cart.");
                request.getRequestDispatcher(ERROR).forward(request, response);
            }
        } else {
            session.setAttribute("message", "Attempted to remove non-existent item from Cart.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
    }

    private void logItemRemoval(String username, String itemId) {
        LogService logService = new LogService();
        String logInfo = "User " + username + " removed item " + itemId + " from cart.";
        logService.insertLogInfo(username, logInfo);
    }
}

