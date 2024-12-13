import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";

const ErrorModal = (props) => {
    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered >
           {/* <Modal.Header closeButton></Modal.Header>*/}
            <Modal.Body className={"Error-Message"} >
                {props.errormessage}
            </Modal.Body>
        </Modal>
    );
}

export default ErrorModal;
