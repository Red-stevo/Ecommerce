import "./Styles/UserProfilePage.css";
import {Image} from "react-bootstrap";
import {MdCircleNotifications, MdEmail} from "react-icons/md";
import {LuPackage2} from "react-icons/lu";
import {TiShoppingCart} from "react-icons/ti";
import {FaTruckArrowRight} from "react-icons/fa6";
import {useNavigate} from "react-router-dom";
import {CiEdit} from "react-icons/ci";
import PersonalDetailsModal from "./Components/PersonalDetailsModal.jsx";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    getUserProfile,
    UpdateEmail, updateProfileImage, updateUserEmail
} from "../../../ApplicationStateManagement/UserProfileStore/UserProfileReducer.js";
import {useForm} from "react-hook-form";
import {FaEdit} from "react-icons/fa";

const defaultImage = {
    female:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.2oQt34IoSR8xxxC18BxxSAHaHa%26pid%3DApi&f=1&ipt=7dc022e1de22d506b7a32e3d13af6881373b07fde2f687f4346ab332ff88d2be&ipo=images",
    male:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.d7mM-baDdCa6BOkxDwQdDwHaH6%26pid%3DApi&f=1&ipt=31109a06cc8020942c4fa80eca12537d34d498ed928ee25d22e910fd71fab11c&ipo=images",
    defaultI:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.explicit.bing.net%2Fth%3Fid%3DOIP.RJHoTYI8wI7PtFxNzWXbwAHaHa%26pid%3DApi&f=1&ipt=12c1c2d6eae5a2cf82cbbff9450ea4fff43ea9cbaab13dbd21b1d0b10f645fc6&ipo=images"
}


const linksList = [
    {name:"Track Order", icon:<FaTruckArrowRight />,link:"/home/user/order-status" },
    {name:"Cart", icon:<TiShoppingCart />,link:"/home/user/cart" },
    {name:"Wish List", icon:<LuPackage2 />,link:"/home/user/wish-list" }
]


const UserProfilePage = () => {
    const {male, female, defaultI} =defaultImage;
    const {userProfileDetails, loading, success, error} = useSelector(state => state.UserProfileReducer);
    const {profileImageUrl, address, email
        , fullName, gender, phoneNumber, username} = userProfileDetails;
    const navigate = useNavigate();
    const [modalShow, setModalShow] = useState(false);
    const [readEmail, setReadEmail] = useState(false);
    const dispatch = useDispatch();
    const {reset, handleSubmit, register
    } = useForm();
    const [userData, setUserData] = useState(null);

    useEffect(() => {
        const userId = "c63b";
        dispatch(getUserProfile(userId));
    }, []);

    useEffect(() => {
        reset({email});
    }, [email]);

    const updateUserData = () => {
        setUserData({userId: "c63b", fullName, gender, address, phoneNumber});
        setModalShow(true);
    }

    const handleUpdateEmail = (email) => {
        setReadEmail((prevState) => !prevState);
        const userId = "c63b";
        const data = {userId, email:email.email};
        dispatch(UpdateEmail(data));

        dispatch(updateUserEmail(email.email));
    }

    const handleImageChange = (event) => {
        const userId = "c63b";
        const upload = event.target.files[0];

        if (!upload) return;

        /*Check the file size, if too large ignore the file.*/
        if (upload.size > 10485760) return;

        dispatch(updateProfileImage({userId, upload}));
    }



    return (
        <div className={"user-profile-page"}>
            <PersonalDetailsModal userdata={userData} show={modalShow} onHide={() => setModalShow(false)} />
            <section className={"user-profile-top-section"}>

                <div className={"notification-details"}>

                    <Image className={"user-profile-image-url"} src={
                        profileImageUrl ? profileImageUrl : gender === "MALE" ? male : gender === "FEMALE" ? female : defaultI}/>

                    <div className={"user-profile-details"}>
                        <span className={"user-profile-username"}>{username}</span>
                        <span className={"user-profile-email"}>{email}</span>
                    </div>

                    <div className={"image-profile-input-holder"}>
                        <label htmlFor={"image-profile-input"}>
                            <FaEdit className={"image-profile-input-icon"}/>
                        </label>
                        <input  onChange={handleImageChange}
                            id={"image-profile-input"} type={"file"} className={"input-profile-image"}/>
                    </div>

                </div>

                <button className={"notifications-button"}>
                    <span className={"Notifications"}>Notifications</span>
                    <MdCircleNotifications className={"notifications-icon"}/>
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
                <button className={"edit-details-button"}  onClick={updateUserData}>
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

            <section className={"user-profile-email-section"}>
                <span className={"email-header"}>My Email Address</span>

                {readEmail ?
                    <input className={"form-control email-input"} type={"text"} placeholder={"email"}
                           {...register("email") } /> :
                    email &&
                    <span className={"user-profile-email-holder"}>
                        <MdEmail className={"email-icon"}/>
                        <span className={"user-profile-email"}>{email}</span>
                    </span>
                }

                {!readEmail ? <button onClick={() => setReadEmail((prevState) => !prevState)}
                        className={"add-email-button"}>Change Email
                </button> :
                    <button onClick={handleSubmit(handleUpdateEmail)}
                        className={"add-email-button"}>Save</button>}

            </section>


        </div>
    );
};

export default UserProfilePage;