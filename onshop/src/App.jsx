import './App.css'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import MainHeader from "./IndexPage/Pages/IndexPage/MainHeader.jsx";
import IndexBody from "./IndexPage/Pages/IndexPage/IndexBody.jsx";
import LoginPage from "./IndexPage/Pages/UserManagementPages/LoginPage.jsx";
import RegistrationPage from "./IndexPage/Pages/UserManagementPages/RegistrationPage.jsx";
import CategoricalProductsDisplay from "./IndexPage/Pages/ProductsDisplayPage/CategoricalProductsDisplay.jsx";
import ProductDisplay from "./IndexPage/Pages/ProductDisplayPage/ProductDisplay.jsx";

const App = () => {
  return (
      <Router>
          <Routes>
              <Route path={"/"} element={<MainHeader />}>
                 <Route path={"/"} element={<IndexBody />}/>
                  <Route path={"/home/products/:productsCategory"} element={<CategoricalProductsDisplay />}/>
                  <Route path={"/home/product/:productId"} element={<ProductDisplay />} />
              </Route>
              <Route path={"/auth/login"} element={<LoginPage />} />
              <Route path={"/auth/registration"} element={<RegistrationPage />} />
          </Routes>
      </Router>
  )
}

export default App
