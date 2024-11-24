import "./Styles/LoginPage.css";
import {Button, Form, Image} from "react-bootstrap";
import {Link} from "react-router-dom";
import googleIcon from "./../../../assets/google-icon.png";

const LoginPage = () => {
    return (
        <div className={"login-page"}>
            <div className={"login-page-left"}>
                <span className={"top-message"}>Welcome <br/>back!</span>
                <span className={"gambi-slogan"}>
                    Unataka ngapi juu we are
                    <span className={"gambi"}>
                        GAMBI <br/>
                    </span> and we have everything
                </span>
            </div>
            <div className={"login-page-right"}>
                <div className={"login-page-right-holder"}>
                    <span className={"login"}>Login</span>
                    <span className={"welcome-message"}>Welcome back, please login to your account</span>
                    <Form className={"login-form"}>
                        <Form.Group>
                            <input id={"username"} type={"text"} className={"form-control username-input"} placeholder={"Username"}/>
                        </Form.Group>

                        <Form.Group>
                            < input id={"password"} type={"password"} className={"form-control password-input"} placeholder={"Password"}/>
                        </Form.Group>
                        <span className={"rem-and-forgot"}>
                             <Form.Check inline label="Remember Me" name="group1" type={'checkbox'} id={`remember-me`}/>
                            <Link className={"forgot-password-link"} to={"#"}>Forgot password?</Link>
                        </span>
                        <div className={"login-button-holder"}>
                            <Button className={"login-button app-button"}>Login</Button>
                        </div>
                    </Form>
                    <span className={"or-sign-in"}>or signin with</span>
                    <Image src={googleIcon} width={30} height={30} className={"google-icon"}/>
                    <span className={"register-user-link"}>
                        New User?
                        <Link className={"sign-up-link"} to={"/auth/registration"}>
                            Sign Up
                        </Link>
                    </span>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;