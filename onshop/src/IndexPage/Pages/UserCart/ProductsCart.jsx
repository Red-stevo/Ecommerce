import "./Styles/ProductsCart.css";
import {Button, Image} from "react-bootstrap";
import {useEffect, useState} from "react";
import {ImCheckboxChecked, ImCheckboxUnchecked} from "react-icons/im";
import CartProduct from "./Components/CartProduct.jsx";
import {FaMoneyBill1Wave} from "react-icons/fa6";
import {PiArrowFatLeftThin, PiArrowFatLineLeftThin, PiArrowFatLinesLeftThin} from "react-icons/pi";
import StarRating from "../ProductsDisplayPage/Components/StarRating.jsx";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {getCartItems} from "../../../ApplicationStateManagement/UserCartStore/CartReducer.js";
import noCartImage from "./../../../assets/NoCartItems.png";



const cartItemsResponses = {
    username:"Stephen Muiru",
    cartId:"AS43DF",
    cartItemsResponses:[
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,inStock:true, color:"grey",count:3,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA102", productPrice:2450.00,inStock:false, color:"grey",count:1,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA103", productPrice:2450.00,inStock:true, color:"grey",count:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA104", productPrice:2450.00,inStock:true, color:"grey",count:5,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA105", productPrice:2450.00,inStock:true, color:"grey",count:1,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA106", productPrice:2450.00,inStock:false, color:"grey",count:3,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA107", productPrice:2450.00,inStock:false, color:"grey",count:9,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },

    ],
    youMayLikes:[
        {productId:"ASD31", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD33", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD34", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD35", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD36", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD37", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD38", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD39", productName:"LapLaptopLaptopLaptoptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD310", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD311", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
    ],
    currentPage:0,
    totalPages:5,
    totalProductPrice:24500,
}

const ProductsCart = () => {
    const [checkIcon, setCheckIcon] = useState(false);
    const {username, cartId, cartItemsResponses, currentPage,
            totalPages, hasMore, youMayLikes, totalProductPrice
    } = useSelector(state => state.CartReducer.CartResponse);
    const [index, setIndex] = useState(0);
    const [selectedProducts, setSelectedProducts] = useState([]);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [page, setPage] = useState(currentPage ? currentPage : 0);

    useEffect(() => {
        const cartData = {page, size:10, userId:"b69eb7ae-d567-45b8-a6a0-92c7f243874f"}
        dispatch(getCartItems(cartData));
    }, [page]);


    useEffect(() => {
        if (selectedProducts.length !== cartItemsResponses.length) setCheckIcon(false);
        else setCheckIcon(true);
    }, [selectedProducts, cartItemsResponses]);


    useEffect(() => {
        setTimeout(() => {
            if (index === 0)
                setIndex(1);
            else if (index === 1)
                setIndex(2);
            else
                setIndex(0);

        }, 500);
    }, [index]);

    const handleDeselection = () => {
        setCheckIcon(false);
        setSelectedProducts(() => []);
    }


    if (!cartItemsResponses || cartItemsResponses.length === 0){
        return (
            <div className={"no-cart-items"}>
                <span className={"title-empty-list"}>Oops! Your Cart is Empty.</span>
                <Image src={noCartImage} className={"no-cart-items-image"}/>
            </div>
        );
    }


    const handleDeleteCart = () => {
        console.log(selectedProducts);
    }

    return (
        <div className={"cart-page"}>
        {/*Page header.*/}
                <div className={"top-buttons"}>
                    <div className={"select-all-holder"}>
                        {checkIcon ?
                            <ImCheckboxChecked className={"select-checked"} onClick={handleDeselection}/> :
                            <ImCheckboxUnchecked className={"select-unchecked"} onClick={() => setCheckIcon(true)}/>}
                        <span>select all</span>
                    </div>
                    <Button disabled={selectedProducts.length === 0}
                            className={"danger-button delete-order-product"} onClick={handleDeleteCart}>
                        Delete
                    </Button>
                </div>

                {/*Product Display.*/}
                <div className={"ordered-products-display"}>
                    {cartItemsResponses && cartItemsResponses.map(
                        ({
                             productId, productPrice, productName, productImageUrl,
                             color, inStock, count
                         }) => (
                            <CartProduct productPrice={productPrice} productName={productName} inStock={inStock}
                                         setSelectedProducts={setSelectedProducts} count={count} color={color}
                                         selectAllCheck={checkIcon} unCheckAll={selectedProducts}
                                         id={productId} productImageUrl={productImageUrl} key={productId}/>
                        ))}


                    {/*Price display*/}
                    <div className={"total-price-shop"}>
                        <div className={"total-price"}><span className={"total"}>Total</span>ksh {totalProductPrice}
                        </div>

                        <div className={"button-shop"}>
                        <span className={"continue-shopping"} title={"Continue Shopping"}>
                            <PiArrowFatLeftThin title={"Continue Shopping"}
                                                className={`${index === 0 ? "unhidden-arrow" : "hidden-arrow"}`}/>
                            <PiArrowFatLineLeftThin title={"Continue Shopping"}
                                                    className={`${index === 1 ? "unhidden-arrow" : "hidden-arrow"}`}/>
                            <PiArrowFatLinesLeftThin title={"Continue Shopping"}
                                                     className={`${index === 2 ? "unhidden-arrow" : "hidden-arrow"}`}/>
                        </span>
                            <span className={"checkout-button"}>
                            <FaMoneyBill1Wave className={"money-bill"}/>Check out
                        </span>
                        </div>
                    </div>
                </div>

                {/*You may also like*/}
                <section className={"you-may-also-like-section"}>
                    <span className={"you-may-like-header"}>You May Also Like</span>

                    <div className={"you-may-like-products"}>
                        {youMayLikes && youMayLikes.map((
                            {
                                productId, productPrice, productName,
                                productImageUrl, rating
                            }) => (
                            <div key={productId} className={"you-may-like-product"}
                                 onClick={() => navigate(`/home/product/${productId}`)}>
                                <Image className={"you-may-like-product-image"} src={productImageUrl}/>
                                <span title={productName}>
                                        {productName.substring(0, 12)} {productName.length > 12 && "..."}
                                    </span>
                                <span className={"price"}>
                                        ksh {productPrice}
                                    </span>
                                <StarRating value={rating} active={true}/>
                            </div>
                        ))}
                    </div>

                </section>
                <div className={"load-more-button-holder"}>
                    <Button onClick={() => setPage(prevState => prevState + 1)}
                            className={"app-button load-more-button"}>Load More</Button>
                </div>
            </div>
    );
}

    export default ProductsCart;