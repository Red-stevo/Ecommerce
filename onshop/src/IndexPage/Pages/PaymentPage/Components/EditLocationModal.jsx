import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";

const EditLocationModal= (props) => {
    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Enter Phone And Location
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <Form className={"user-details-form"}>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Full Name"} type={"tel"} />
                    </FormGroup>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button className={"app-button"}></Button>
            </Modal.Footer>
        </Modal>
    );
}

export default EditLocationModal;
