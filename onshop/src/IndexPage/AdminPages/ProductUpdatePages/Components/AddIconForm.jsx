import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Form, FormGroup, Image} from "react-bootstrap";
import {MdCloudUpload} from "react-icons/md";
import FileReview from "./FileReview.jsx";
import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {useDispatch, useSelector} from "react-redux";
import {
    deleteCategories,
    postCategory,
    putCategories
} from "../../../../ApplicationStateManagement/CategoriesStore/CategoriesReducer.js";
import Loader from "../../../../Loading/Loader.jsx";

const AddIconForm= (props) => {
    const [iconUpload, setIconUpload] = useState(null);
    const [iconPreview, setIconPreview] = useState([]);
    const {register,
        handleSubmit,
        reset} = useForm();
    const dispatch = useDispatch();
    const {errorMessage, loading, success} = useSelector(state => state. CategoriesReducer);


    useEffect(() => {

        if (props.show === true){
            if (props.editdata) reset({categoryName:props.editdata.categoryName});

            else reset({categoryName:""});

        }

    }, [props]);

    useEffect(() => {
        if (iconPreview.length === 0) setIconUpload(null);
    }, [iconPreview]);


    const handleFileChange = (event) => {
        const files = Array.from(event.target.files);

        {files.length > 0 && files.forEach((file) => {

            if (!file) return;

            /*Check the file size, if too large ignore the file.*/
            if (file.size > 10485760) return;

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

    const handleCategorySubmit = (data) => {

        if (!props.editdata) {

            const categoryData = {
                categoryName:data.categoryName, file:iconUpload,
            }

            dispatch(postCategory(categoryData));

            /*Clean up states*/
            reset({categoryName : "",});
            setIconPreview([]);
            setIconUpload(null);
        }else {
            const updateCategoryData = {categoryName: "", categoryIcon:null, categoryId:props.editdata.categoryId}

            if (iconUpload){
                updateCategoryData.categoryName = data.categoryName;
                updateCategoryData.categoryIcon = iconUpload;
            }else updateCategoryData.categoryName = data.categoryName;

            dispatch(putCategories(updateCategoryData));

            /*Clean up states*/
            setIconPreview([]);
            setIconUpload(null);
            props.editdata.setModalShow(false);

            console.log("Update hit");
        }
    }



    const deleteCategory = () => {
        dispatch(deleteCategories(props.editdata.categoryId));
        props.editdata.setModalShow(false);
    }



    return (
        <Modal{...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered  className={"modal-pop"}>
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Add Product Icon
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <Form className={"user-details-form"}>

                    <FormGroup>
                        <input className={"form-control"} placeholder={"Category Name"} type={"text"}
                               {...register("categoryName")} />
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


                        {props.editdata && iconPreview.length === 0 &&
                            <Image className={"preview-icon-url"} src={props.editdata.categoryIcon} />}

                        <FileReview handleRemove={handleIconDelete} previewImages={iconPreview} />
                    </div>

                </Form>

            </Modal.Body>
            <Modal.Footer>
                <div className={"category-form-buttons"}>
                    {props.editdata && <Button onClick={deleteCategory} className={"app-button"}>Delete</Button>}
                    <Button onClick={handleSubmit(handleCategorySubmit)} className={"app-button"}>
                        {props.editdata ? "Update" : "Add"}
                    </Button>
                </div>
            </Modal.Footer>

            {loading && <Loader />}
        </Modal>
    );
}

export default AddIconForm;
