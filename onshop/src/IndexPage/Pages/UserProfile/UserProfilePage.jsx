import "./Styles/UserProfilePage.css";
import {Image} from "react-bootstrap";
import {MdCircleNotifications} from "react-icons/md";
import {LuPackage2} from "react-icons/lu";
import {TiShoppingCart} from "react-icons/ti";
import {FaTruckArrowRight} from "react-icons/fa6";
import {useNavigate} from "react-router-dom";
import {CiEdit} from "react-icons/ci";
import Button from "react-bootstrap/Button";
import PersonalDetailsModal from "./Components/PersonalDetailsModal.jsx";
import {useState} from "react";



const userProfileDetails = {
    userProfileImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.d7mM-baDdCa6BOkxDwQdDwHaH6%26pid%3DApi&f=1&ipt=31109a06cc8020942c4fa80eca12537d34d498ed928ee25d22e910fd71fab11c&ipo=images",
    username:"Stevo",
    email:"stephenmuiru@gmail.com",
    fullName:"Stephen Muiru",
    phoneNumber:"+254110555949",
    address:"Kimathi way, Nyeri, Kenya.",
    gender:"Male"
}


const defaultImage = {
    female:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.2oQt34IoSR8xxxC18BxxSAHaHa%26pid%3DApi&f=1&ipt=7dc022e1de22d506b7a32e3d13af6881373b07fde2f687f4346ab332ff88d2be&ipo=images",
    male:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.d7mM-baDdCa6BOkxDwQdDwHaH6%26pid%3DApi&f=1&ipt=31109a06cc8020942c4fa80eca12537d34d498ed928ee25d22e910fd71fab11c&ipo=images",
    default:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.explicit.bing.net%2Fth%3Fid%3DOIP.RJHoTYI8wI7PtFxNzWXbwAHaHa%26pid%3DApi&f=1&ipt=12c1c2d6eae5a2cf82cbbff9450ea4fff43ea9cbaab13dbd21b1d0b10f645fc6&ipo=images"
}


const linksList = [
    {name:"Track Order", icon:<FaTruckArrowRight />,link:"/home/user/order-status" },
    {name:"Cart", icon:<TiShoppingCart />,link:"/home/user/cart" },
    {name:"Wish List", icon:<LuPackage2 />,link:"/home/user/wish-list" }
]


const UserProfilePage = () => {
    const {userProfileImage, address, email
        , fullName, gender, phoneNumber, username} = userProfileDetails;
    const navigate = useNavigate();
    const [modalShow, setModalShow] = useState(false);


    return (
        <div className={"user-profile-page"}>
            <PersonalDetailsModal show={modalShow} onHide={() => setModalShow(false)}/>
            <section className={"user-profile-top-section"}>

                <div className={"notification-details"}>

                    <Image className={"user-profile-image-url"} src={userProfileImage} />

                    <div className={"user-profile-details"}>
                        <span className={"user-profile-username"}>{username}</span>
                        <span className={"user-profile-email"}>{email}</span>
                    </div>

                </div>

               <button className={"notifications-button"}>
                   <span className={"Notifications"}>Notifications</span>
                   <MdCircleNotifications className={"notifications-icon"} />
                   <span className={"notifications-count"}>0</span>
               </button>

            </section>

            <section className={"user-page-links"}>

                {linksList.map(({icon, link, name}, index) => (
                    <button className={"user-profile-page-link"} key={index} onClick={() => navigate(link)}>
                        <span className={"user-profile-page-icon"}>{icon}</span>
                        <span className={"user-profile-page-name"}>{name}</span>
                    </button>
                ))}

            </section>


            <section className={"user-personal-details"}>
                <button className={"edit-details-button"} variant="primary" onClick={() => setModalShow(true)}>
                    <CiEdit className={"edit-personal-details"}/>
                    <span className={"edit-personal-details-text"}>Edit</span>
                </button>

                <div className={"personal-details-holder"}>

                    <div className={"personal-details-fields-holder"}>

                        <div className={"personal-details-fields"}>
                            <span className={"personal-details-field"}>FULL NAME : </span>
                            <span className={"personal-details-field"}>{fullName}</span>
                        </div>

                        <div className={"personal-details-fields"}>
                            <span className={"personal-details-field"}>PHONE NUMBER : </span>
                            <span className={"personal-details-field"}>{phoneNumber}</span>
                        </div>
                    </div>

                    <div className={"personal-details-fields-holder"}>

                        <div className={"personal-details-fields"}>
                            <span className={"personal-details-field"}>ADDRESS : </span>
                            <span className={"personal-details-field"}>{address}</span>
                        </div>

                        <div className={"personal-details-fields"}>
                            <span className={"personal-details-field"}>GENDER : </span>
                            <span className={"personal-details-field"}>{gender}</span>
                        </div>

                    </div>

                </div>

            </section>


        </div>
    );
};

export default UserProfilePage;