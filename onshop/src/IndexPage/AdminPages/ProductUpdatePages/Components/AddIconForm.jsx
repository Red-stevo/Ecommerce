import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup} from "react-bootstrap";
import {MdCloudUpload} from "react-icons/md";
import FileReview from "./FileReview.jsx";
import {useState} from "react";

const AddIconForm= (props) => {
    const [iconUpload, setIconUpload] = useState(null);
    const [iconPreview, setIconPreview] = useState([]);

    const handleFileChange = (event) => {
        const files = Array.from(event.target.files);

        {files.length > 0 && files.forEach((file) => {

            if (!file) return;

            /*Check the file size, if too large ignore the file.*/
            if (file.size > 10485760)return;

            /*add file to the upload list.*/
            setIconUpload(() => file);

            const reader = new FileReader();

            // Check if the file is an image or video
            if (file.type.startsWith("image/")) {
                reader.onload = (e) => {
                    setIconPreview(() => [ {file:e.target.result, type:"image"}]);
                };
                reader.readAsDataURL(file);

            } else if (file.type.startsWith("video/") || file.type.startsWith("application/")) {
                reader.onload = (e) => {
                    setIconPreview(() => [{file:e.target.result, type:"video"}]);
                };
                reader.readAsDataURL(file);
            } else {
                console.log(`File starts with ${file.type}`);
                console.error("File type not supported. Please upload an image or video.");
            }
        })}
    };

    const handleIconDelete = () => {
        setIconPreview([]);
    }





    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Add Product Icon
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <Form className={"user-details-form"}>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Category Name"} type={"text"}/>
                    </FormGroup>

                    <div className={"images-review"}>
                        <>
                            <label htmlFor="fileUpload" className="custom-label">
                                <MdCloudUpload className={"upload-icon"}/>
                                upload
                            </label>
                            <input onChange={handleFileChange}
                                type="file" multiple={true} accept="image/*" id="fileUpload"
                                className="file-input-filled"/>
                        </>
                        <FileReview handleRemove={handleIconDelete} previewImages={iconPreview} />
                    </div>


                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default AddIconForm;
