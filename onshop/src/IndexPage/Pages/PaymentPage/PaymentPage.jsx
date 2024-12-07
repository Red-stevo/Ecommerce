import "./Styles/PaymentPage.css";
import {CiEdit} from "react-icons/ci";
import {PiMapPinAreaFill} from "react-icons/pi";
import mpesaIcon from "./../../../assets/mpesaPaymentIcon.svg";
import {Image, InputGroup} from "react-bootstrap";
import {useEffect, useState} from "react";

const paymentDetails = {
    username:"Bob Mirowe",
    phoneNumber: "+254790785612",
    location:"Wabera Street Nairobi",
    products:[
        {productId:"AS43D1",productName:"Best HP laptops in 2021 | Laptop Mag",productPrice:2340.50, productCount:3, productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.B0qQ-7ZyU4fMsJUEC0qokgHaEK%26pid%3DApi&f=1&ipt=edc9259e7b201c57d6101811053e0089d58a04c9da58abe8d7e15ae420ecf2de&ipo=images"},
        {productId:"AS43D2",productName:"Best HP laptops 2023 | TechRadar",productPrice:2540.00, productCount:1, productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.mos.cms.futurecdn.net%2Fj6ndSR6hKav95QT83beRB4.jpg&f=1&nofb=1&ipt=adeb09496e23eb54770ba1637a43db96d7f23fa9bc100742e79f5fc2c997e7dc&ipo=images"},
        {productId:"AS43D3",productName:"Best HP laptops in 2021 | Laptop Mag",productPrice:2340.50, productCount:2, productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.B0qQ-7ZyU4fMsJUEC0qokgHaEK%26pid%3DApi&f=1&ipt=edc9259e7b201c57d6101811053e0089d58a04c9da58abe8d7e15ae420ecf2de&ipo=images"},
        {productId:"AS43D4",productName:"Best HP laptops 2023 | TechRadar",productPrice:2540.00, productCount:1, productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.mos.cms.futurecdn.net%2Fj6ndSR6hKav95QT83beRB4.jpg&f=1&nofb=1&ipt=adeb09496e23eb54770ba1637a43db96d7f23fa9bc100742e79f5fc2c997e7dc&ipo=images"},
    ]
}

const PaymentPage = () => {
    const {username, phoneNumber, location, products} = paymentDetails;
    const [productCount, setProductsCount] = useState(0);

    useEffect(() => {
        const setItemsCount = () => {
            setProductsCount(() => 0);
            products.forEach(({productCount}) =>{
                setProductsCount(prevState => prevState + productCount)
            });}
        if (products) setItemsCount();
    }, [products]);


    return (
        <div className={"payment-page"}>

            <section className={"payment-page-location-section"}>

                <div className={"payment-page-location-section-title"}>
                    <span className={"payment-page-location-section-shipping"} >Shipping Information</span>

                    <button className={"payment-page-location-section-edit"} ><CiEdit/> Edit</button>

                </div>

                <div className={"payment-page-location-section-address"}>

                    <PiMapPinAreaFill className={"payment-page-location-section-icon"} />
                    <div className={"payment-page-location-section-phone-location"}>
                        <span className={"payment-page-location-section-phone"}>{username} {phoneNumber}</span>
                        <span className={"payment-page-location-section-location"}>Location {location}</span>
                    </div>

                </div>

            </section>


            <section className={"payment-page-pay-method-section"}>

                <span className={"payment-page-pay-method-section-title"}>Payment Method</span>

                <div className={"payment-page-pay-method-section-number-icon"}>

                    <div className={"payment-page-pay-method-section-icon"}>
                        <Image  src={mpesaIcon} className={"payment-page-pay-method-section-mpesa-icon"}/>
                        <span className={"payment-page-pay-method-section-icon-text"}>
                            Via Gambi Stores Collections
                        </span>
                    </div>

                    <InputGroup className={" payment-page-pay-method-section-number"}>
                        <InputGroup.Text id="basic-addon1">+254</InputGroup.Text>
                        <input className={"form-control payment-page-pay-method-section-number-input"}
                               minLength={9} maxLength={9} type={"tel"}
                            placeholder="Phone Number" aria-label="Phone Number" aria-describedby="basic-addon1"/>
                    </InputGroup>

                </div>

            </section>


            <section className={"payment-page-products-section"}>

                <span className={"payment-page-products-section-product-title-count"}>
                    Product List ( {productCount} Items)
                </span>

                <div className={"payment-page-products-section-products"}>
                    {products && products.map((
                            {productCount, productId,
                                productImage, productName, productPrice}) =>  (
                            <div key={productId} className={"payment-page-products-section-product"}>
                                <Image src={productImage}
                                       className={"payment-page-products-section-product-image"}/>

                                <span className={"payment-page-products-section-product-name"}>{productName}</span>

                                <div className={"payment-page-products-section-product-price-count"}>
                                    <span className={"payment-page-products-section-product-price"}>
                                        ksh {productPrice}
                                    </span>
                                    <span className={"payment-page-products-section-product-count"}>
                                        X {productCount}
                                    </span>
                                </div>

                            </div>
                        )
                    )}
                </div>

            </section>


        </div>
    );
};

export default PaymentPage;


