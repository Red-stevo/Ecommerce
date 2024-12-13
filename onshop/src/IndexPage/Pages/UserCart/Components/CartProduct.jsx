import {ImCheckboxChecked, ImCheckboxUnchecked} from "react-icons/im";
import {useEffect, useState} from "react";
import {Image} from "react-bootstrap";
import {FaMinus, FaPlus} from "react-icons/fa";
import {useDispatch} from "react-redux";
import {updateQuantity} from "../../../../ApplicationStateManagement/UserCartStore/CartReducer.js";

const CartProduct = (
    {productPrice, productName, productImageUrl, inStock, color, count, id, setSelectedProducts, selectAllCheck,
        cartId, unCheckAll}) => {
    const [select, setSelect] = useState(false);
    const [productCount, setProductCount] = useState(count ? count : 0);
    const dispatch = useDispatch();


    useEffect(() => {
        if (productCount <= 0)
            setProductCount(1);
    }, [productCount]);


    useEffect(() => {
        if (select)
            setSelectedProducts(prevState => [...prevState, id]);
        else
            setSelectedProducts(preState => preState.filter((productId => productId !== id)));
    }, [select]);


    useEffect(() => {
        if (selectAllCheck) setSelect(true);
    }, [selectAllCheck]);

    useEffect(() => {
        if (unCheckAll.length === 0) setSelect(false);
    }, [unCheckAll]);

    useEffect(() => {
        const data = {cartId, cartItemId:id,quantity:productCount};
        if(productCount > count || productCount < count) dispatch(updateQuantity(data));

    }, [productCount]);

    return (
        <div className={"cart-product-details"}>
            <div onClick={() => setSelect(!select)} className={"select-icon"}>{
                select ? <ImCheckboxChecked className={"select-checked"} />:
                    <ImCheckboxUnchecked className={"select-unchecked"} />
            }</div>

            <Image src={productImageUrl} className={"product-image"} />

            <div className={"name-amount-holder"}>

                <div className={"name-stock"}>
                    <span className={"cart-product-name"}>{productName}</span>
                    {!inStock && <span className={"out-stock"}>Out Of Stock</span>}
                </div>

                <div className={"amount-price"}>
                    <span className={"product-color"}>Color : {color}</span>
                    <span><FaMinus className={"vary-quantity"} onClick={() => setProductCount(productCount - 1)}/>
                        <span className={"cart-quantity"}>{productCount}</span>
                        <FaPlus className={"vary-quantity"} onClick={() => setProductCount(productCount + 1)} />
                    </span>
                    <span className={"product-price-cart"}>ksh {productPrice}</span>
                </div>
            </div>
        </div>
    );
};

export default CartProduct;