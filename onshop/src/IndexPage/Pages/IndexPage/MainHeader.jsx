import {Button, Image, InputGroup, Navbar} from "react-bootstrap";
import {FaSearch} from "react-icons/fa";
import CategoryDropDown from "./Components/IndexHeader/CategoryDropDown.jsx";
import Cart from "./Components/IndexHeader/Cart.jsx";
import UserAccount from "./Components/IndexHeader/UserAccount.jsx";
import "./Styles/MainHeader.css";
import {Outlet, useNavigate} from "react-router-dom";
import logo from "./../../../assets/applicationLogo.png";
import {useState} from "react";
const MainHeader = () => {
    const [searchContents, setSearchContents] = useState("");
    const navigate = useNavigate();

    const handleKeyDown = (event) => {if (event.key === "Enter" && searchContents) handleSearchProduct();}


    const handleSearchProduct = () => {

        if (searchContents.trim().length > 2) {
            const formattedUrl =
                encodeURIComponent((searchContents.trim()).toLowerCase().replaceAll(' ', '+'));
            navigate(`/home/products/${formattedUrl}`);
            setSearchContents("");
        }
    }

    return (
        <>
            <Navbar expand="md" className="main-header">
                <Navbar.Brand href="#" className={"app-logo"}>
                    <Image src={logo} className={"app-logo-icon"} />
                </Navbar.Brand> {/*Application logo*/}
                <Navbar.Toggle aria-controls="basic-navbar-nav" className={"toggle-icon"}/>
                <Navbar.Collapse id="basic-navbar-nav" className={"main-header-content-holder"}>
                    <InputGroup className="mx-2 mb-2 search-and-buttons">
                        <CategoryDropDown />    {/*Categories drop-down*/}

                        <input className={"form-control search-bar"} placeholder={"Search"} required={true}
                               value={searchContents} aria-label="Product Search" aria-describedby="basic-addon2"
                               onChange={(event) => setSearchContents(event.target.value)}
                               onKeyDown={handleKeyDown}

                        /> {/*Search bar*/}
                        <Button variant="outline-secondary" onClick={handleSearchProduct}
                                 id="search-button" className={"search-buttons-right"}>
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