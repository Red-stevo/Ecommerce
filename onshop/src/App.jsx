import './App.css'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import MainHeader from "./IndexPage/Pages/IndexPage/MainHeader.jsx";
import IndexBody from "./IndexPage/Pages/IndexPage/IndexBody.jsx";
import LoginPage from "./IndexPage/Pages/UserManagementPages/LoginPage.jsx";
import RegistrationPage from "./IndexPage/Pages/UserManagementPages/RegistrationPage.jsx";
import CategoricalProductsDisplay from "./IndexPage/Pages/ProductsDisplayPage/CategoricalProductsDisplay.jsx";
import ProductDisplay from "./IndexPage/Pages/ProductDisplayPage/ProductDisplay.jsx";
import AdminstrationSideBar from "./IndexPage/AdminPages/AdminstrationSideBar.jsx";
import AddProductPage from "./IndexPage/AdminPages/ProductUpdatePages/AddProductPage.jsx";
import AllOrdersPage from "./IndexPage/AdminPages/OrdersPages/AllOrdersPage.jsx";
import OrderDisplay from "./IndexPage/AdminPages/OrdersPages/OrderDisplay.jsx";
import ProductsCart from "./IndexPage/Pages/UserCart/ProductsCart.jsx";
import OrderStatus from "./IndexPage/Pages/OrderStatusPage/OrderStatus.jsx";
import WishListPage from "./IndexPage/Pages/WishListPage/WishListPage.jsx";
import SingleProductDisplay from "./IndexPage/AdminPages/OrdersPages/SingleProductDisplay.jsx";

const App = () => {
  return (
      <Router>
          <Routes>
              <Route path={"/"} element={<MainHeader />}>
                 <Route path={"/"} element={<IndexBody />}/>
                  <Route path={"/home/products/:productsCategory"} element={<CategoricalProductsDisplay />}/>
                  <Route path={"/home/product/:productId"} element={<ProductDisplay />} />
                  <Route path={"/home/user/cart"} element={<ProductsCart />} />
                  <Route path={"/home/user/order-status"} element={ <OrderStatus /> }/>
                  <Route path={"/home/user/wish-list"} element={<WishListPage />}/>
              </Route>
              <Route path={"/auth/login"} element={<LoginPage />} />
              <Route path={"/auth/registration"} element={<RegistrationPage />} />
              <Route path={"/admin"} element={<AdminstrationSideBar />}>
                    <Route path={"/admin/newProduct"} element={<AddProductPage />} />
                    <Route path={"/admin"} element={<AllOrdersPage />} />
                    <Route path={"/admin/orders/:orderId"} element={<OrderDisplay />} />
                    <Route path={"/admin/orders/:orderId/:productId"} element={<SingleProductDisplay />} />
              </Route>
          </Routes>
      </Router>
  )
}

export default App
