import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getCategories} from "../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import "./Styles/ProductUpdatePage.css";
import {FiPlusCircle} from "react-icons/fi";
import {FloatingLabel} from "react-bootstrap";
import {IoIosClose} from "react-icons/io";
import {GoTriangleDown, GoTriangleUp} from "react-icons/go";

const ProductUpdatePage = () => {
    const {categories} = useSelector(state => state.CategoriesReducer);
    const [selectedCategories, setSelectedCategories] = useState(categories ? categories : []);
    const dispatch = useDispatch();
    const productCategories = ["Shoes", "Toys", "Health Care"];


    useEffect(() => {
        dispatch(getCategories());
    }, []);


    return (
        <div className={"product-update-page"}>
            <input className={"product-update-page-name-update"} type={"text"}/>
            <FloatingLabel className={"product-description product-update-page-description-update"}
                           controlId="floatingTextarea"
                           label="Product Decription">
                            <textarea required={true} className={"input-field form-control"}
                                placeholder={"Product Description"} style={{height: '100px'}} id={"productDescription"}/>
            </FloatingLabel>

            <section className={"product-update-page-categories"}>
                {productCategories && productCategories.length > 0 &&
                    productCategories.map((category, index) => (
                        <div key={index} className={"categories-preview"}>
                            {category}
                            <IoIosClose className={"cancel-categories"}/>
                        </div>
                    ))
                }
                <input className={"input-category-select"} type={"text"}/>
            </section>

            <div>
                <input className={"product-update-page-variety-update"} placeholder={"variety"}/>

                <input className={"product-update-page-proportion-update"} placeholder={"proportion"}/>
            </div>

            <div className={"proportion-variety-update-input"}>
                <input className={"product-update-page-price-update"} placeholder={"price"}/>

                <input className={"product-update-page-discount-update"} placeholder={"discount"}/>
            </div>

            <div className={"product-update-page-count-triangle"}>
                <GoTriangleUp className={"product-update-page-triangle"} />
                <input className={"product-update-page-count-update"} placeholder={"count"}/>
                <GoTriangleDown className={"product-update-page-triangle"} />
            </div>

        </div>
    );
};

export default ProductUpdatePage;