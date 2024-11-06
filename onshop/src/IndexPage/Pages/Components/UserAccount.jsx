import {RiAccountCircleLine} from "react-icons/ri";
import {Button} from "react-bootstrap";

const UserAccount = () => {
    return (
        <Button className={"user-account-button"}>
            <RiAccountCircleLine className={"user-account-icon"} />
            <span className={"my-account"}>My Account</span>
        </Button>
    );
};

export default UserAccount;