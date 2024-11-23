import {IoMdArrowDropdown, IoMdArrowDropright} from "react-icons/io";
import {useState} from "react";
import {Link} from "react-router-dom";
import "./../AdminstrationSideBar.css";

// eslint-disable-next-line react/prop-types
const MenuToggleComponent = ({productIcon=null, menuTitle="", childrenLink= []}) => {
    const [productMenuToggle, setProductMenuToggle] = useState(false);

    return (
        <div className={"products-pages main-links"} onClick={() => setProductMenuToggle(!productMenuToggle)}>
            {productIcon}
            {menuTitle}
            {productMenuToggle ?
                <IoMdArrowDropdown className={"product-arrow"} /> : <IoMdArrowDropright  className={"product-arrow"}/>}

            {productMenuToggle && childrenLink.length > 0 && childrenLink.map(({title, link}, index) => (
                <div key={index} className={"page-links-section"}>
                    <Link to={`/admin/${link}`} className={"page-link-text"}>
                        {title}
                    </Link>
                </div>
            ))}

        </div>
    );
};

export default MenuToggleComponent;