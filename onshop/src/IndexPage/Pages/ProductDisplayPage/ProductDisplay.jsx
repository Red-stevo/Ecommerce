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


    useEffect(() => {
        dispatch(getProductDetails(null));
        const structureData = () => {
            let sizes = [];

            products.forEach(({ productSize }) => {
                sizes.push(productSize);
            });
            setSizes(new Set(sizes.sort()));
        }

        if(products && products.length > 0 ) structureData();

    }, [products]);



    const updateAvailableProducts = (activeSize) => {

        setActiveSize(activeSize); /*Set the active product to update the hover event listener*/
        setAvailableProducts([]); /*Reset the list of available products for the proportion*/

        /*Get a list of products for a given proportion*/
        if (size && Array.from(size).length > 0) {
            products.forEach((product) => {
                if (product.productSize === activeSize) setAvailableProducts(prevState => ([...prevState, product]));
            });
        }

        /*Get the list available varieties.*/
        if (availableProducts && availableProducts.length > 0) {
            /*Reset available colors*/
            setAvailableColors([]);

            availableProducts.forEach((product) => {
                setAvailableColors(prevState => [...prevState, product.productColor]);
            });
        }

    }






    return (
        <div className={"product-page"}>
            <div className={"product-details-display"}>
                <div>
                    <Image height={100} width={80} />
                    <div>
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
                                                             onMouseEnter={() => updateAvailableProducts(size)}>
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
                                                             onMouseEnter={1}>
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