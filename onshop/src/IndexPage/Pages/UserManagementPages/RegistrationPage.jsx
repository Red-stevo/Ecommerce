import "./Styles/RegistrationPage.css";
import {Button, Form, Image} from "react-bootstrap";
import googleIcon from "./../../../assets/google-icon.png";


const RegistrationPage = () => {

    return (


        <div className={"registration-page"}>
           <div className={"registration-page-left"} /> {/*Left image holder for desktop view reg page.*/}

            <div className={"registration-page-right"}>
                <div className={"registration-page-right-holder"}>

                    {/*Registration page header*/}
                    <span className={"welcome-reg"}>
                        Welcome to
                        <span className={"site-name"}>
                            Gambi Collection!
                        </span>
                    </span>

                    <span className={"create-account"}>
                        Create an Account
                    </span>

                    {/*Registration form*/}
                    <Form className={"registration-form"}>
                        <Form.Group>
                            <input className={"form-control reg-username-input"} placeholder={"Username"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control reg-email-input"} placeholder={"Email"}/>
                        </Form.Group>

                        <Form.Group className={"both-passwords-input"}>
                            <input type={"password"} className={"form-control reg-password-input"} placeholder={"Password"}/>

                            <input type={"password"} className={"form-control reg-confirm-password-input"} placeholder={"Confirm Password"}/>
                        </Form.Group>


                        <div className={"reg-and-login-buttons"}>
                            <Button className={"register"}>REGISTER</Button>
                            <Button href={"/auth/login"} className={"sign-in"}>SIGN IN</Button>
                        </div>

                    </Form>

                    <div className={"alternative-register"}>
                        <span className={"sign-up-google"}>Sign Up with, <br /></span>
                        <Image src={googleIcon} className={'google-icon'} height={30} />
                    </div>

                    <Form.Check inline className={"disclaimer"}
                                label=" By Registering your details, you agree to our
                    Terms of Service and Privacy Policy" name="group1" type={'checkbox'} id={`disclaimer`}/>
                </div>
            </div>
        </div>
    );
};

export default RegistrationPage;