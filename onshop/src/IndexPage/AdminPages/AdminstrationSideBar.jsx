import {Outlet} from "react-router-dom";
import "./AdminstrationSideBar.css";
import {IoCloseCircleOutline} from "react-icons/io5";
import {AiFillProduct} from "react-icons/ai";
import MenuToggleComponent from "./Components/MenuToggleComponent.jsx";
import {FaProductHunt} from "react-icons/fa";
import {useState} from "react";
import {PiListDashesFill} from "react-icons/pi";
import {FiMenu} from "react-icons/fi";


const AdministrationSideBar = () => {
    const [hide, setHide] = useState(true);

    return (
        <>
            {hide && <FiMenu className={"dash-board-toggle"} onClick={() => setHide(!hide)} /> }
            <div className={`dash-board ${hide && "hide-element"}`}>
                <span className={"admin-title"}>
                    ADMIN
                    <IoCloseCircleOutline className={"close-button"} title={"Close"} onClick={() => setHide(!hide)} />
                </span>
                <span className={"dashboard-title"}>
                    <AiFillProduct className={"squares"} />
                    Dashboard
                </span>
                <div className={"link-holder"}>

                </div>

                <MenuToggleComponent
                    productIcon={<FaProductHunt className={"product-icon"}/>}
                    menuTitle={"Products"}
                    childrenLink={[
                        {title:"Add Product", link:"newProduct"},
                        {title:"Product List", link:"productList"},
                        {title:"Categories", link:"productCategories"},
                    ]}
                />


            </div>


            <div className={"admin-page-base"} onClick={() => setHide(true)}>
                <Outlet />
            </div>
        </>
    );
};

export default AdministrationSideBar;