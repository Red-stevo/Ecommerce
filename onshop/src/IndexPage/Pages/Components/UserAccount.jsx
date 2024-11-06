import {RiAccountCircleLine} from "react-icons/ri";
import {Button} from "react-bootstrap";

const UserAccount = () => {
    return (
        <Button>
            <RiAccountCircleLine className={"user-account-icon"} />
        </Button>
    );
};

export default UserAccount;