import {Outlet} from "react-router-dom";
import "./AdminstrationSideBar.css";
import {IoCloseCircleOutline} from "react-icons/io5";
import {AiFillProduct} from "react-icons/ai";
import MenuToggleComponent from "./Components/MenuToggleComponent.jsx";
import {FaProductHunt} from "react-icons/fa";
import {useState} from "react";
import {PiListDashesFill} from "react-icons/pi";
import {HiShoppingCart} from "react-icons/hi";


const AdministrationSideBar = () => {
    const [hide, setHide] = useState(true);

    return (
        <>
            {hide && <PiListDashesFill className={"dash-board-toggle"} onClick={() => setHide(!hide)} /> }
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

                {/*First menu bunch*/}
                <MenuToggleComponent
                    productIcon={<FaProductHunt className={"product-icon"}/>}
                    menuTitle={"Products"}
                    childrenLink={[
                        {title:"add product", link:"newProduct"},
                        {title:"product list", link:"productList"},
                        {title:"categories", link:"productCategories"},
                    ]}
                />

                {/*Second menu bunch*/}
                <MenuToggleComponent
                    productIcon={<HiShoppingCart className={"product-icon"}/>}
                    menuTitle={"Order"}
                    childrenLink={[{title:"all orders", link:"orders"},]}/>

            </div>


            <div className={"admin-page-base"} onClick={() => setHide(true)}>
                <Outlet />
            </div>
        </>
    );
};

export default AdministrationSideBar;