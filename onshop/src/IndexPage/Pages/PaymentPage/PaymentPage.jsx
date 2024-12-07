import "./Styles/PaymentPage.css";
import {CiEdit} from "react-icons/ci";
import {FaLocationDot} from "react-icons/fa6";

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
                    <span>Shipping Information</span>
                    <span>Edit </span> <CiEdit/><span/>
                </div>

                <div className={"payment-page-location-section-address"}>
                    <FaLocationDot />
                    <div>
                        <span>{username} {phoneNumber}</span>
                        <span>Location {location}</span>
                    </div>
                </div>


            </section>

        </div>
    );
};

export default PaymentPage;


