import {IoIosClose} from "react-icons/io";

const FileReview = ({previewImages, handleRemove}) => {

    return (
        <div className={"preview-holder"}>
            {previewImages.length > 0 && previewImages.map(({file, type}, index) => (
                <div key={index}>
                    {type === "image" && (
                        <div className={"preview-image-video"} >
                            <img src={file} alt="Preview" className={"preview-item"} />
                            <IoIosClose className={"cancel-categories"} onClick={() => handleRemove(index)} />
                        </div>
                    )}

                    {type === "video" && (
                        <div className={"preview-image-video"}>
                            <video src={file} controls className={"preview-item"} />
                            <IoIosClose className={"cancel-categories"} onClick={() => handleRemove(index)} />
                        </div>
                    )}
                </div>
            ))
            }
        </div>
    );
};

export default FileReview;
