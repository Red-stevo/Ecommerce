import logo from "./../../../assets/logo.png";
import {Image} from "react-bootstrap";

const Footer = () => {
    return (
        <div>
            <div>
                <div>
                    <Image src={logo} className={"app-logo"} />
                </div>
            </div>
        </div>
    );
};

export default Footer;