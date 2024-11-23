import {Outlet} from "react-router-dom";
import "./AdminstrationSideBar.css";
import {IoCloseCircleOutline} from "react-icons/io5";
import {AiFillProduct} from "react-icons/ai";
import MenuToggleComponent from "./Components/MenuToggleComponent.jsx";
import {FaProductHunt} from "react-icons/fa";


const AdministrationSideBar = () => {

    return (
        <>
            <div className={"dash-board"}>
                <span className={"admin-title"}>
                    ADMIN
                    <IoCloseCircleOutline className={"close-button"} title={"Close"} />
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


            <div className={"admin-page-base"}>
                <h1>Hello</h1>
                <Outlet />
            </div>
        </>
    );
};

export default AdministrationSideBar;