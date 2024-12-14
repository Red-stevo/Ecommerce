import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";
import {useState} from "react";
import {combineSlices} from "@reduxjs/toolkit";
import {useDispatch} from "react-redux";
import {fetchLocation} from "../../../../ApplicationStateManagement/OpenCage/openCageReducer.js";

const EditLocationModal= (props) => {
    const [query, setQuery] = useState("");
    const dispatch = useDispatch();
    const handleSearch = () => {
        dispatch(fetchLocation(query))
    }

    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Enter Phone Number And Delivery Location
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <Form className={"user-details-form"}>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Full Name"} type={"tel"} />
                    </FormGroup>

                    <FormGroup>
                        <input value={query}
                            onChange={(event) => setQuery(event.target.value)}
                            type={"text"} placeholder={"Search Location"} />
                    </FormGroup>
                    <Button onClick={handleSearch}>Search</Button>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button className={"app-button"}></Button>
            </Modal.Footer>
        </Modal>
    );
}

export default EditLocationModal;
