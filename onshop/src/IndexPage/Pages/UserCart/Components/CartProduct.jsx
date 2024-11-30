import {ImCheckboxChecked, ImCheckboxUnchecked} from "react-icons/im";
import {useEffect, useState} from "react";
import {Image} from "react-bootstrap";
import {FaMinus, FaPlus} from "react-icons/fa";

const CartProduct = ({productPrice, productName, productImageUrl, inStock, color,count}) => {
    const [select, setSelect] = useState(false);
    const [productCount, setProductCount] = useState(count);


    useEffect(() => {
        if (productCount <= 0)
            setProductCount(1);
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
                    <span>{productName}</span>
                    {!inStock && <span className={"out-stock"}>Out Of Stock</span>}
                </div>

                <div className={"amount-price"}>
                    <span className={"product-color"}>Color : {color}</span>
                    <span><FaMinus className={"vary-quantity"} onClick={() => setProductCount(productCount - 1)}/>
                        <span className={"cart-quantity"}>{productCount}</span>
                        <FaPlus className={"vary-quantity"} onClick={() => setProductCount(productCount + 1)} />
                    </span>
                    <span>ksh {productPrice}</span>
                </div>
            </div>
        </div>
    );
};

export default CartProduct;