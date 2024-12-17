import {MdCloudUpload} from "react-icons/md";
import FileReview from "./FileReview.jsx";

const FileUploadPreview = (props)  => {
    return <div className={"images-review"}>
        <>
            <label htmlFor="fileUpload" className="custom-label">
                <MdCloudUpload className={"upload-icon"}/>
                upload
            </label>
            <input onChange={props.onChange}
                   type="file" multiple={true} accept="image/*, video/*, application/*" id="fileUpload"
                   className="file-input-filled"/>
        </>
        <FileReview previewImages={props.previewImages} handleRemove={props.handleRemove}/>
    </div>;
}

export default FileUploadPreview;