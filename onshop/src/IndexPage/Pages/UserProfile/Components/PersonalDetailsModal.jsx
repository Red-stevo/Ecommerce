import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";
import {useEffect} from "react";
import {useForm} from "react-hook-form";
import {useDispatch} from "react-redux";
import {updateUserData} from "../../../../ApplicationStateManagement/UserProfileStore/UserProfileReducer.js";

const PersonalDetailsModal= (props) => {
    const {reset, register, handleSubmit
    } = useForm();
    const dispatch = useDispatch();

    useEffect(() => {
        reset({...props.userdata});
    }, [props.userdata]);

    const handleUserDataUpdate = (data) => {
       dispatch(updateUserData(data));
    }

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
                               {...register("phoneNumber")} />
                    </FormGroup>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Address"} type={"text"}
                               {...register("address")} />
                    </FormGroup>

                    <select defaultChecked={true} className={"form-select"} {...register("gender")}>
                        <option value={"NONE"}>Select Gender</option>
                        <option value={"MALE"}>Male</option>
                        <option value={"FEMALE"}>Female</option>
                        <option value={"NOT_SPECIFIED"}>Rather Not Say</option>
                    </select>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button className={"app-button"} onClick={handleSubmit(handleUserDataUpdate)}>Update</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default PersonalDetailsModal;
