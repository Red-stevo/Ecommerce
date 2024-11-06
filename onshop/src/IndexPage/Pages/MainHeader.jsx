import {Button, InputGroup, Navbar} from "react-bootstrap";
import {FaSearch} from "react-icons/fa";
import CategoryDropDown from "./Components/CategoryDropDown.jsx";
import Cart from "./Components/Cart.jsx";
import UserAccount from "./Components/UserAccount.jsx";
import "./../Styles/MainHeader.css";

const MainHeader = () => {
    return (
            <Navbar expand="lg" className="main-header">
                <Navbar.Brand href="#" className={"app-logo"}>OnShop Logo</Navbar.Brand> {/*Application logo*/}
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <InputGroup className="mx-2 mb-2 search-and-buttons">
                        <CategoryDropDown />    {/*Categories drop-down*/}
                        <input className={"form-control search-bar"} placeholder={"Search"}
                              aria-label="Product Search" aria-describedby="basic-addon2"
                        /> {/*Search bar*/}
                        <Button variant="outline-secondary" id="search-button">
                            <FaSearch className={"search-icon"}/>
                        </Button> {/*Search Button(icon)*/}
                    </InputGroup>
                    <UserAccount /> {/*User Account icon.*/}
                    <Cart /> {/*Add to cart button*/}
                </Navbar.Collapse>
            </Navbar>
    );
};

export default MainHeader;