import "./Styles/PaymentPage.css";
import {CiEdit} from "react-icons/ci";
import {PiMapPinAreaFill} from "react-icons/pi";

const paymentDetails = {
    username:"Bob Mirowe",
    phoneNumber: "+254790785612",
    location:"Wabera Street Nairobi"
}

const PaymentPage = () => {
    const {username, phoneNumber, location} = paymentDetails;


    return (
        <div className={"payment-page"}>

            <section className={"payment-page-location-section"}>

                <div className={"payment-page-location-section-title"}>
                    <span className={"payment-page-location-section-shipping"} >Shipping Information</span>

                    <button className={"payment-page-location-section-edit"} >Edit <CiEdit/></button>

                </div>

                <div className={"payment-page-location-section-address"}>

                    <PiMapPinAreaFill className={"payment-page-location-section-icon"} />
                    <div className={"payment-page-location-section-phone-location"}>
                        <span className={"payment-page-location-section-phone"}>{username} {phoneNumber}</span>
                        <span className={"payment-page-location-section-location"}>Location {location}</span>
                    </div>

                </div>


            </section>

        </div>
    );
};

export default PaymentPage;


