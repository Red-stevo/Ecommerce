import { useEffect, useState, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getProductDetails } from "../../../ApplicationStateManagement/productStore.js";
import { Button, Image, Pagination } from "react-bootstrap";
import "./ProductDisplay.css";

const ProductDisplay = () => {
    const { products, productName, productDescription } = useSelector(
        (state) => state.productReducer.product
    );
    const dispatch = useDispatch();

    // State
    const [activeSize, setActiveSize] = useState(null);
    const [activeColor, setActiveColor] = useState(null);
    const [productOnDisplay, setProductOnDisplay] = useState(null);


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



    // Automatically set the first available color if the active size changes
    useEffect(() => {
        if (availableColors.length > 0) {
            setActiveColor(availableColors[0]);
        }
    }, [availableColors]);



    return (
        <div className={"product-page"}>
            <div className={"product-details-display"}>
                <div>
                    <Image height={160} width={140} />
                    <div>
                        {productOnDisplay &&
                            productOnDisplay.productImages.map((imageUrl, index) => (
                                <Image
                                    src={imageUrl}
                                    height={30}
                                    width={20}
                                    key={index}
                                    alt={`product image ${index}`}
                                />
                            ))}
                    </div>
                </div>
                <div>
                    <div>
                        <Button>Details</Button>
                        <Button>Reviews</Button>
                    </div>
                    <span>{productName}</span>
                    <section>{productDescription}</section>
                    <div>
                        <div>
                            <span>
                                Proportion:
                                <Pagination>
                                    {sizes.map((size, index) => (
                                        <Pagination.Item
                                            key={index}
                                            active={activeSize === size}
                                            onClick={() => setActiveSize(size)}
                                        >
                                            {size}
                                        </Pagination.Item>
                                    ))}
                                </Pagination>
                            </span>
                            <span>
                                Variety:
                                <Pagination>
                                    {availableColors.map((color, index) => (
                                        <Pagination.Item
                                            key={index}
                                            active={activeColor === color}
                                            onClick={() => setActiveColor(color)}
                                        >
                                            {color}
                                        </Pagination.Item>
                                    ))}
                                </Pagination>
                            </span>
                        </div>
                        <span>Price:{productOnDisplay && productOnDisplay.productNewPrice}</span>
                    </div>
                </div>
            </div>
            <div>
                <span>Related Products</span>
            </div>
            <div>
                <span>Product Reviews</span>
            </div>
        </div>
    );
};

export default ProductDisplay;
