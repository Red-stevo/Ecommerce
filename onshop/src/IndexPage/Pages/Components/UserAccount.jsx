import {RiAccountCircleLine} from "react-icons/ri";
import {Button} from "react-bootstrap";

const UserAccount = () => {
    return (
        <Button className={"user-account-button"}>
            <RiAccountCircleLine className={"user-account-icon"} />
            <h6>Account</h6>
        </Button>
    );
};

export default UserAccount;