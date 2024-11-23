
// eslint-disable-next-line react/prop-types
import {IoIosClose} from "react-icons/io";

// eslint-disable-next-line react/prop-types
const FileReview = ({previewImages}) => {

    return (
        <div className={"preview-holder"}>
            {/* eslint-disable-next-line react/prop-types */}
            {previewImages.length > 0 && previewImages.map(({file, type}, index) => (
                <div key={index}>
                    {type === "image" && (
                        <div className={"preview-image-video"} >
                            <img src={file} alt="Preview" className={"preview-item"} />
                            <IoIosClose className={"cancel-categories"} />
                        </div>
                    )}

                    {type === "video" && (
                        <div className={"preview-image-video"}>
                            <video src={file} controls className={"preview-item"} />
                            <IoIosClose className={"cancel-categories"} />
                        </div>
                    )}
                </div>
            ))
            }
        </div>
    );
};

export default FileReview;
