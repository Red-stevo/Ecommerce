import "./Styles/AddProductPage.css";
import {Button, FloatingLabel, Form, InputGroup} from "react-bootstrap";
import {MdCloudUpload} from "react-icons/md";
import {useEffect, useState} from "react";
import FileReview from "./Components/FileReview.jsx";
import {IoIosClose} from "react-icons/io";


const categories = [
    {categoryName:"men", categoryId:1},
    {categoryName:"Utensils", categoryId:2},
    {categoryName:"Electronics", categoryId:3},
    {categoryName:"Women", categoryId:4},
    {categoryName:"Clothes", categoryId:5},
    {categoryName:"Shoes", categoryId:6},
]





const AddProductPage = () => {
    const [uploads, setUploads] = useState([]);
    const [previewFile, setPreviewFile] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);

    const removeCategory=(category)=>{setSelectedCategories(selectedCategories.filter((item) => item !== category));}

    const handleImageRemove = (imageIndex) => {
       setUploads(uploads.filter((_,index) => index !== imageIndex));
       setPreviewFile(previewFile.filter((_, index) => index !== imageIndex))
    }


    const handleFileChange = (event) => {
        const files = Array.from(event.target.files);

        {files.length > 0 && files.forEach((file) => {

            if (!file) return;

            /*Check the file size, if too large ignore the file.*/
            if (file.size > 10485760){
                console.log("The File '",file.name,"' is too large");
                return;
            }


            /*add file to the upload list.*/
            setUploads((prevState) => [...prevState, file]);

            const reader = new FileReader();

            // Check if the file is an image or video
            if (file.type.startsWith("image/")) {
                reader.onload = (e) => {
                    setPreviewFile((prevState) => [...prevState, {file:e.target.result, type:"image"}]);
                };
                reader.readAsDataURL(file);

            } else if (file.type.startsWith("video/") || file.type.startsWith("application/")) {
                reader.onload = (e) => {
                    setPreviewFile((prevState) => [...prevState, {file:e.target.result, type:"video"}]);
                };
                reader.readAsDataURL(file);
            } else {
                console.log(`File starts with ${file.type}`);
                console.error("File type not supported. Please upload an image or video.");
            }
        })}
    };


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

                    {/*Preview the selected categories.*/}
                    <div className={"categories-preview-holder"}>
                        {/*Pre view selected categories*/}
                        {selectedCategories.length > 0 && selectedCategories.map((category, index) => (
                            <div key={index} className={"categories-preview"}>
                                {category} <IoIosClose className={"cancel-categories"}
                                                       onClick={() => removeCategory(category)} />
                            </div>))
                        }
                    </div>

                    <select className={"form-select"}
                        onChange={(e) =>
                            setSelectedCategories((prevState) => [...prevState, e.target.value])}>

                        {categories.length > 0 && categories.map(({categoryId, categoryName}) => (
                            <option key={categoryId} value={categoryName}>{categoryName}</option>))
                        }
                    </select>

                    <div className={"size-color"}>
                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Variety"}/>
                        </Form.Group>

                        <Form.Group>
                            <input className={"form-control"} placeholder={"Product Proportion"}/>
                        </Form.Group>
                    </div>

                    <div className={"price-count"}>
                        <InputGroup className="">
                            <Form.Control className={"productPrice"}
                                          aria-label="Product Price" placeholder={'Product Price'} />
                            <Form.Control className={"productDiscount"}
                                          aria-label="Product discount" placeholder={"Product discount"} />
                        </InputGroup>

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
                            <input onChange={handleFileChange}
                                type="file" multiple={true} accept="image/*, video/*, application/*" id="fileUpload"
                                className="file-input-filled" />
                        </>
                        <FileReview previewImages={previewFile} handleRemove={handleImageRemove} />
                    </div>


                    <div className={"submit-buttons"}>
                        <Button className={"app-button"}>Save</Button>
                        <Button className={"app-button"}>Publish</Button>
                    </div>
                </Form>
            </div>
        </div>
    );
};

export default AddProductPage;