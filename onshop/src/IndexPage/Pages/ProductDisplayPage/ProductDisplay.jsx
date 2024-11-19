import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getProductDetails} from "../../../ApplicationStateManagement/productStore.js";
import {Button, Image, Pagination} from "react-bootstrap";
import "./ProductDisplay.css";


const ProductDisplay = () => {
    const {products, productName, productDescription} = useSelector(state => state.productReducer.product);
    const dispatch = useDispatch();
    const [size, setSizes] = useState(new Set());
    const [activeSize, setActiveSize] = useState(null);
    const [availableProducts, setAvailableProducts] = useState([]);
    const [availableColors, setAvailableColors] = useState([]);
    const [activeColor, setActiveColor] = useState(null);
    const [productOnDisplay, setProductOnDisplay] = useState(null);


    /*Fetch product from the backend.*/
    useEffect(() => {
        dispatch(getProductDetails(null));

        /*Structure the product details.*/
        const structureData = () => {
            let sizes = [];
            setSizes(new Set());
            products.forEach(({ productSize }) => {
                sizes.push(productSize);
            });
            setSizes(new Set(sizes.sort()));
        }
        if(products && products.length > 0 ) structureData();

    }, [products, ]);

    useEffect(() => {

        /*Initializing the product display*/
        const setInitialProduct = () => {

            const {productColor, productSize} = products[0];
            updateAvailableProducts(productSize);
            updateProductOnDisplay(productColor);
        }


        if (size && Array.from(size).length > 0) {
            setInitialProduct();
        }
    }, [size]);



    const updateAvailableProducts = (activeSize) => {

        setActiveSize(activeSize); /*Set the active product to update the hover event listener*/
        setAvailableProducts([]); /*Reset the list of available products for the proportion*/


        /*Get a list of products for a given proportion*/
        if (size && Array.from(size).length > 0) {
            products.forEach((product) => {
                if (product.productSize === activeSize) setAvailableProducts(prevState => [...prevState, product]);
            });
        }
    }


    useEffect(() => {
        setAvailableColors([]);  /*Reset available colors*/

        /*Get the list available varieties.*/
        if (availableProducts && availableProducts.length > 0) {
            availableProducts.forEach((product) => {
                setAvailableColors(prevState => [...prevState, product.productColor]);
            });
        }
    }, [availableProducts]);


    const updateProductOnDisplay = (color) => {

        /*Update the color toggle*/
        setActiveColor(color);

        /*Set Product to be display to the user.*/
        availableProducts.forEach((product) => {
            if (product.productColor === color) setProductOnDisplay(product);
        });
    }



        /*Testing*/
    useEffect(() => {
        if(productOnDisplay) {
            console.log(productOnDisplay);
            console.log(productOnDisplay.productImages);
        }
    }, [productOnDisplay]);


    return (
        <div className={"product-page"}>
            <div className={"product-details-display"}>
                <div>
                    <Image height={160} width={140} />
                    <div>
                        {productOnDisplay && productOnDisplay.productImages && productOnDisplay.productImages.map((imageUrl, index) => (
                            <Image src={imageUrl} height={30} width={20} key={index} alt={`product image ${index}`}  />
                        ))}
                    </div>
                </div>
                <div>
                    <div>
                        <Button>Details</Button>
                        <Button>Reviews</Button>
                    </div>
                    <span>
                        {productName}
                    </span>
                    <section>
                        {productDescription}
                    </section>
                    <div>
                        <div>
                            <span>
                                Proportion :
                                <Pagination>
                                    {
                                        Array.from(size).map((size, index) => (
                                            <Pagination.Item key={index} active={activeSize === size}
                                                             onClick={() => updateAvailableProducts(size)}>
                                                {size}
                                            </Pagination.Item>
                                        ))
                                    }
                              </Pagination>
                            </span>
                            <span>
                               Variety :
                                 <Pagination>
                                    {
                                        availableColors.map((color, index) => (
                                            <Pagination.Item key={index} active={activeColor === color}
                                                             onClick={() => updateProductOnDisplay(color)}>
                                                {color}
                                            </Pagination.Item>
                                        ))
                                    }
                              </Pagination>
                            </span>
                        </div>
                        <span>
                            Price : {}
                        </span>
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