import { useEffect, useState, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getProductDetails } from "../../../ApplicationStateManagement/productStore.js";
import { Button, Image, Pagination } from "react-bootstrap";
import "./ProductDisplay.css";
import {FaRegHeart} from "react-icons/fa";
import {TiShoppingCart} from "react-icons/ti";
import {LuShare2} from "react-icons/lu";
import {useNavigate} from "react-router-dom";
import StarRating from "../ProductsDisplayPage/Components/StarRating.jsx";

const ProductDisplay = () => {
    const { products, productName, productDescription, relatedProducts, productReviews } = useSelector(
        (state) => state.productReducer.product);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // State
    const [activeSize, setActiveSize] = useState(null);
    const [activeColor, setActiveColor] = useState(null);
    const [productOnDisplay, setProductOnDisplay] = useState(null);
    const [activeImage, setActiveImage] = useState('');


    // Fetch products on component mount
    useEffect(() => {
        dispatch(getProductDetails(null));
    }, [dispatch]);

    // Extract unique sizes
    const sizes = useMemo(() => {
        if (!products || products.length === 0) return [];
        return [...new Set(products.map((product) => product.productSize))].sort();
    }, [products]);



    // Filter products by active size
    const availableProducts = useMemo(() => {
        if (!activeSize || !products) return [];
        return products.filter((product) => product.productSize === activeSize);
    }, [products, activeSize]);



    // Extract colors from available products
    const availableColors = useMemo(() => {
        return availableProducts.map((product) => product.productColor);
    }, [availableProducts]);



    // Initialize active size, color, and product display
    useEffect(() => {
        if (products && products.length > 0) {
            const { productSize, productColor } = products[0];
            setActiveSize(productSize);
            setActiveColor(productColor);
        }
    }, [products]);



    // Update displayed product when active color changes
    useEffect(() => {
        if (activeColor) {
            const product = availableProducts.find(
                (product) => product.productColor === activeColor
            );
            setProductOnDisplay(product || null);
        }
    }, [activeColor, availableProducts]);


    //set a default image on display.
    useEffect(() => {
        if (productOnDisplay){
            setActiveImage(productOnDisplay.productImages[0]);
        }
    }, [productOnDisplay]);

    // Automatically set the first available color if the active size changes
    useEffect(() => {
        if (availableColors.length > 0) {
            setActiveColor(availableColors[0]);
        }
    }, [availableColors]);


    return (
        <div className={"product-page"}>
            <div className={"product-details-display"}>
                <span className={"product-display-name2"}>{productName}</span> {/*medium screen display*/}

                <div className={"conversion-buttons3"}>
                    <Button className={"add-cart-button app-button"}>
                        <TiShoppingCart className={"add-cart-icon"}/>
                        ADD TO CART
                    </Button>
                    <Button className={"order-button app-button"}>ORDER NOW</Button>
                    <LuShare2 className={"share-icon"} title={"Share"}/>
                </div> {/*Not in medium screens*/}

                <div className={"image-display-holder"}>

                    <div className={"image-view"}>
                        <Image src={activeImage} className={"large-image-view"}/>
                        <div className={"toggle-images-holder"}>
                            {productOnDisplay &&
                                productOnDisplay.productImages.map((imageUrl, index) => (
                                    <Image className={"toggle-image"} src={imageUrl} height={30}
                                           width={20} key={index} alt={`product image ${index}`}
                                           onMouseOver={() => setActiveImage(imageUrl)} />
                                ))}
                        </div>
                    </div>


                    <div className={"conversion-section"}>

                        <div className={"product-prices-display2"}>
                                <span className={"new-product-price-display"}>
                                    ksh {productOnDisplay && productOnDisplay.productNewPrice}
                                </span>
                            {(productOnDisplay && productOnDisplay.productOldPrice < productOnDisplay.productNewPrice)
                                &&
                                <span className={"old-product-price-display"}>
                                    save ksh {(productOnDisplay.productNewPrice - productOnDisplay.productOldPrice)}
                                </span>
                            }
                        </div>{/*Medium screen display.*/}

                        {/*Available colours medium screen*/}
  {/*Check*/}                      <Pagination className={"available-colors  list-toggle"}>
                            {availableColors.map((color, index) => (
                                <Pagination.Item key={index} active={activeColor === color}
                                                 className="portion" onClick={() => setActiveColor(color)}>
                                    {color}
                                </Pagination.Item>
                            ))}
                        </Pagination> {/*Not In medium screen*/}


                        {/*Conversion buttons.*/}
                        <div className={"conversion-buttons2"}>

                            <div className={"cart-order-buttons"}>
                                <Button className={"add-cart-button app-button"}>
                                    <TiShoppingCart className={"add-cart-icon"}/>
                                    ADD TO CART
                                </Button>
                                <Button className={"order-button app-button"}>ORDER NOW</Button>
                            </div>

                            <div className={"share-count-buttons"}>

                                <LuShare2 className={"share-icon"} title={"Share"}/>

                                {productOnDisplay &&
                                    <span className={"available-count2"}>
                                        Available Products
                                        <span className={"product-count"}>
                                            {productOnDisplay.productCount}
                                        </span>
                                    </span>
                                }
                            </div>

                        </div> {/*Medium screen display*/}


{/*Check*/}                        <Pagination className={"available-colors2 list-toggle"}>
                            {availableColors.map((color, index) => (
                                <Pagination.Item key={index} active={activeColor === color}
                                                 className="portion" onClick={() => setActiveColor(color)}>
                                    {color}
                                </Pagination.Item>
                            ))}
                        </Pagination> {/*Medium screen display.*/}


                        <div className={"price-and-proportions2"}>

  {/*Check*/}                          <Pagination className={"product-sizes-holder list-toggle"}>
                                {sizes.map((size, index) => (
                                    <Pagination.Item key={index} active={activeSize === size}
                                                     className={"portion"} onClick={() => setActiveSize(size)}>
                                        {size}
                                    </Pagination.Item>
                                ))}
                            </Pagination>

                            <Button className={"wish-list-button app-button"}><FaRegHeart/>WISHLIST</Button>
                        </div> {/*Medium screen display*/}



                    </div>
                </div>


                <div className={"product-details-display-holder"}>
                    <div className={"conversion-buttons"}>
                        <Button className={"add-cart-button app-button"}>
                            <TiShoppingCart className={"add-cart-icon"}/>
                            ADD TO CART
                        </Button>
                        <Button className={"order-button app-button"}>ORDER NOW</Button>
                        <LuShare2 className={"share-icon"} title={"Share"}/>
                    </div> {/*Not in medium screens*/}


                    <span className={"product-display-name"}>{productName}</span> {/*Not in medium screens*/}


                    <div className={"product-prices-display"}>
                            <span className={"new-product-price-display"}>
                                ksh {productOnDisplay && productOnDisplay.productNewPrice}
                            </span>
                        {(productOnDisplay && productOnDisplay.productOldPrice < productOnDisplay.productNewPrice)
                            &&
                            <span className={"old-product-price-display"}>
                                save ksh {(productOnDisplay.productNewPrice - productOnDisplay.productOldPrice)}
                            </span>
                        }
                    </div> {/*No in medium screens.*/}


                    <div className={"price-and-proportions"}>
 {/*check*/}                       <Pagination className="product-sizes-holder list-toggle">
                            {sizes.map((size, index) => (
                                <Pagination.Item key={index} active={activeSize === size}
                                    className="portion" onClick={() => setActiveSize(size)}>
                                    {size}
                                </Pagination.Item>
                            ))}
                        </Pagination>

                        <Button className={"wish-list-button app-button"}><FaRegHeart/>WISHLIST</Button>
                    </div> {/*Not in medium screens.*/}

                    {productOnDisplay &&
                        <span className={"available-count"}>
                            Available Products
                            <span className={"product-count"}>
                                {productOnDisplay.productCount}
                            </span>
                        </span>/*Not in medium screens*/
                    }


                    <section className={"product-display-description"}>{productDescription}</section>


                </div>
            </div>
            <div>
                <span className={"header-related-products"}>Related Products</span>
                <div className={"related-products-holder"}>
                    {relatedProducts.length > 0 && relatedProducts.map((product) => (
                        <div key={product.productId} className={"related-product"}
                             onClick={() => navigate(`/home/product/${product.productId}`)}>
                            <Image src={product.productImage} className={"related-product-image"}/>
                            <span className={"related-product-name"}>{product.productName}</span>
                            <span className={"related-product-price"}>ksh {product.productPrice}</span>
                        </div>
                    ))}
                </div>
            </div>
            <div className={"review-section"}>
                <span className={"product-reviews-section"}>Product Reviews</span>
                <div className={"product-reviews-holder"}>
                    {productReviews.length > 0 &&  productReviews.map((review, index) => (
                        <div key={index} className={"review"}>
                            <span className={"review-username"}>{review.username}</span>
                            <span className={"review-content"}>{review.reviewContent}</span>
                            <StarRating active={true} value={review.rating}/>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default ProductDisplay;