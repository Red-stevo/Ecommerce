import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";
import {useEffect} from "react";
import {useForm} from "react-hook-form";

const PersonalDetailsModal= (props) => {
    const {reset, register, handleSubmit
    } = useForm();

    useEffect(() => {
        reset({...props.userdata});
    }, [props.userdata]);

    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Enter User Details
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <Form className={"user-details-form"}>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Full Name"} type={"text"}
                               {...register("fullName")}/>
                    </FormGroup>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Phone Number"} type={"tel"}
                               {...register("gender")} />
                    </FormGroup>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Address"} type={"text"}
                               {...register("gender")} />
                    </FormGroup>

                    <select defaultChecked={true} className={"form-select"}>
                        <option value={1}>Select Gender</option>
                        <option value={"MALE"}>Male</option>
                        <option value={"FEMALE"}>Female</option>
                        <option value={"NOT_SPECIFIED"}>Rather Not Say</option>
                    </select>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default PersonalDetailsModal;
