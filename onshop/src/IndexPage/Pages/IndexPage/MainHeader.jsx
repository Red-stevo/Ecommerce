import {Button, InputGroup, Navbar} from "react-bootstrap";
import {FaSearch} from "react-icons/fa";
import CategoryDropDown from "../Components/IndexHeader/CategoryDropDown.jsx";
import Cart from "../Components/IndexHeader/Cart.jsx";
import UserAccount from "../Components/IndexHeader/UserAccount.jsx";
import "../../Styles/MainHeader.css";
import {Outlet} from "react-router-dom";

const MainHeader = () => {
    return (
        <>
            <Navbar expand="md" className="main-header">
                <Navbar.Brand href="#" className={"app-logo"}>OnShop Logo</Navbar.Brand> {/*Application logo*/}
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav" className={"main-header-content-holder"}>
                    <InputGroup className="mx-2 mb-2 search-and-buttons">
                        <CategoryDropDown />    {/*Categories drop-down*/}
                        <input className={"form-control search-bar"} placeholder={"Search"}
                              aria-label="Product Search" aria-describedby="basic-addon2"
                        /> {/*Search bar*/}
                        <Button variant="outline-secondary" id="search-button" className={"search-buttons-right"}>
                            <FaSearch className={"search-icon"}/>
                        </Button> {/*Search Button(icon)*/}
                    </InputGroup>
                    <div className={"header-end-icons"}>
                        <UserAccount /> {/*User Account icon.*/}
                        <Cart /> {/*Add to cart button*/}
                    </div>
                </Navbar.Collapse>
            </Navbar>

            <div className={"pages-body"}>
                <Outlet />
            </div>
        </>
    );
};

export default MainHeader;