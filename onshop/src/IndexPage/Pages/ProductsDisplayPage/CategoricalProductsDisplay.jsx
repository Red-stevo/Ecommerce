import "./Styles/CategoricalProductsDisplay.css";
import CategoriesMenu from "./Components/CategoriesMenu.jsx";
import {Button, Image} from "react-bootstrap";
import StarRating from "./Components/StarRating.jsx";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {queryProducts} from "../../../ApplicationStateManagement/ProductStores/SearchProducts.js";
import Loader from "../../../Loading/Loader.jsx";

const CategoricalProductsDisplay = () => {
    const navigate = useNavigate();
    const { productsCategory} = useParams();
    const dispatch = useDispatch();
    const {products, page, status} = useSelector(state => state.SearchProducts);
    const [currentPage, setCurrentPage] = useState( 0);

    useEffect(() => {
       const query = productsCategory.replaceAll('+', ' ');
       const data = {query,currentPage};
       dispatch(queryProducts(data));
    }, [productsCategory, currentPage]);


    return (
        <div className={"product-menu-holder"}>
            <CategoriesMenu />

            {status !== "loading" ?
            <div className={"product-display-section"}>
                {products && products.map(
                ({productId, productName, discountedPrice,productRating, productImagesUrl}, index) => (
                    <div className={"product-gen-display"} key={index} onClick={() => {navigate(`/home/product/${productId}`)}}>
                        <Image src={productImagesUrl} alt={productName} className={"product-display-image"} />
                        <div>
                            <span className={"product-display-name"}>{productName}</span>
                            <StarRating active={true} value={productRating}/>
                            <span className={"product-display-price"}>ksh {discountedPrice}</span>
                        </div>

                    </div>
                ))}


                <div className={`load-more-button-holder ${currentPage + 1 >= page.totalPages && " hide "}`}>
                    <Button onClick={() => setCurrentPage(page.number + 1)}
                            className={"load-more-button app-button"}>Load More</Button>
                </div>
            </div>:
                <Loader />
            }
        </div>
    );
};

export default CategoricalProductsDisplay;