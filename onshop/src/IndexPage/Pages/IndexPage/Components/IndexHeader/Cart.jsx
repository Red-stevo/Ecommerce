import {BsCart4} from "react-icons/bs";
import {Button} from "react-bootstrap";

const Cart = () => {
    return (
        <Button className={"cart-button"} href={"/home/user/cart"}>
            <span className={"cart-count"}>12</span>
            <BsCart4 className={"cart-icon"}/>
            <span className={"cart-text"}>Cart</span>
        </Button>
    );
};

export default Cart;