import {RiAccountCircleLine} from "react-icons/ri";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

const UserAccount = () => {
    const navigate = useNavigate();
    return (
        <Button className={"user-account-button"} onClick={() => navigate("/auth/login")}>
            <RiAccountCircleLine className={"user-account-icon"} />
            <span className={"my-account"}>My Account</span>
        </Button>
    );
};

export default UserAccount;