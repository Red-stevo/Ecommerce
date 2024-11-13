import "./Styles/RegistrationPage.css";
import {Button, Form, Image} from "react-bootstrap";


const RegistrationPage = () => {
    return (
        <div className={"registration-page"}>
           <div className={"registration-page-left"}>

           </div>

            <div className={"registration-page-right"}>
                <div className={"registration-page-right-holder"}>
                    <span className={"welcome-reg"}>
                        Welcome to
                        <span className={"site-name"}>
                            Gambi Collection!
                        </span>
                    </span>

                    <span className={"create-account"}>
                        Create an Account
                    </span>

                    <Form>
                        <Form.Group>
                            <input className={"form-control username-input"} placeholder={"Username"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control email-input"} placeholder={"Email"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control reg-password-input"} placeholder={"Password"}/>

                            <input className={"form-control reg-confirm-password-input"} placeholder={"Confirm Password"}/>
                        </Form.Group>


                        <div className={"reg-and-login-buttons"}>
                            <Button className={"register"}>REGISTER</Button>
                            <Button className={"sign-in"}>SIGN IN</Button>
                        </div>

                    </Form>

                    <div className={"alternative-register"}>
                        <span className={"sign-up-google"}>Sign Up with, </span>
                        <Image/>
                    </div>
                    <Form.Check inline label=" By Registering your details, you agree to our
                    Terms of Service and Privacy Policy" name="group1" type={'checkbox'} id={`remember-me`}/>
                </div>
            </div>

        </div>
    );
};

export default RegistrationPage;