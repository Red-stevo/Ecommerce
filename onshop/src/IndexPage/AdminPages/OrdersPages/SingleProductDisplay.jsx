import "./Styles/SingleProductDisplay.css";
import {Image} from "react-bootstrap";
import {useLocation} from "react-router-dom";
import {useEffect} from "react";
import {useSelector} from "react-redux";

const productDetails = {
    cancel:false,
    productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
    productName:"SleekTech Pro 15.6\" Laptop",
    productDescription:"Experience performance and style with the SleekTech Pro. " +
        "Featuring a vibrant 15.6\" Full HD display, " +
        "this lightweight laptop is powered by the latest Intel Core i7 " +
        "processor and 16GB of RAM for seamless multitasking. " +
        "With a 512GB SSD, you'll enjoy lightning-fast storage and quick boot times. " +
        "Designed for productivity and entertainment, it includes a backlit keyboard, long-lasting battery, " +
        "and advanced cooling system. Perfect for professionals and students alike.",
    productQuantity:2,
    productVariety:"grey",
    productProportion:"820 Inches"
}


const SingleProductDisplay = () => {
    const {productDetails} = useSelector(state => state)
    const  location = useLocation();


    useEffect(() => {

    }, []);

    return (
        <div className={"single-order-product-page"}>
            {productDetails.cancel ?
                <span className={"canceled"}>CANCELED!</span>:
                <span className={"active-product"}>ACTIVE</span>
            }

            <section className={"order-product-details"} >

                <Image className={"ordered-product-image-url"} src={productDetails.productImageUrl} />

                <div className={"ordered-product-details-section"}>

                    <span className={"single-order-product-name"}>{productDetails.productName}</span>
                    <span className={"single-order-product-description"}>{productDetails.productDescription}</span>

                    <div className={"quantity-proportion-variety"}>
                        <span className={"single-order-product-quantity"} >Quantity : {productDetails.productQuantity}</span>
                        <span className={"single-order-product-proportion"}>Proportion : {productDetails.productProportion}</span>
                        <span className={"single-order-product-variety"}>Variety : {productDetails.productVariety}</span>
                    </div>

                </div>

            </section>
        </div>
    );
};

export default SingleProductDisplay;