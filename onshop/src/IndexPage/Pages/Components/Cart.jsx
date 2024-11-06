import {BsCart4} from "react-icons/bs";
import {Button} from "react-bootstrap";

const Cart = () => {
    return (
        <Button className={"cart-button"}>
            <BsCart4 className={"cart-icon"}/>
        </Button>
    );
};

export default Cart;