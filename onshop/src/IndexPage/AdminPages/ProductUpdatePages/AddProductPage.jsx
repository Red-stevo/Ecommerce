import "./Styles/AddProductPage.css";
import {FloatingLabel, Form} from "react-bootstrap";
import {MdCloudUpload} from "react-icons/md";
const AddProductPage = () => {
    return (
        <div className={"add-product-page"}>
            <div className={"title-form-buttons-holder"}>
                <span className={"page-title"}>Add Product</span>
                <Form className={"product-input-form"}>
                    <Form.Group>
                        <input className={"form-control"} placeholder={"Product Name"}/>
                    </Form.Group>

                    <FloatingLabel className={"event-description"} controlId="floatingTextarea" label="Event Decription">
                        <textarea
                            required={true}
                            className={"input-field form-control"}
                            placeholder={"Event Description"}
                            style={{ height: '100px' }}/>

                    </FloatingLabel>


                    <Form.Group>
                        <input className={"form-control"} placeholder={"Product Category"}/>
                    </Form.Group>

                    <div className={"size-color"}>
                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Variety"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Proportion"}/>
                        </Form.Group>
                    </div>

                    <div className={"price-count"}>
                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Price"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Count"}/>
                        </Form.Group>
                    </div>

                    <div className={"images-review"}>
                        <>
                            <label htmlFor="fileUpload" className="custom-label">
                                <MdCloudUpload className={"upload-icon"} />
                                upload
                            </label>
                            <input type="file" accept="image/*, video/*" id="fileUpload" className="file-input-filled"/>
                        </>

                    </div>
                </Form>
            </div>
        </div>
    );
};

export default AddProductPage;