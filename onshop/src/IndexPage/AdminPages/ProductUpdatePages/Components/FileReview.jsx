import {IoIosClose} from "react-icons/io";

const FileReview = ({previewImages, handleRemove}) => {

    return (
        <div className={"preview-holder"}>
            {previewImages.length > 0 && previewImages.map((data, index) => (
                <div key={index}>
                    {
                        data.startsWith("http") && (
                            <div className={"preview-image-video"}>
                                <img src={data} alt="Preview" className={"preview-item"}/>
                                <IoIosClose className={"cancel-categories"} onClick={() => handleRemove(data)}/>
                            </div>
                        )
                    }
                    {data.type === "image" && (
                        <div className={"preview-image-video"}>
                            <img src={data.file} alt="Preview" className={"preview-item"}/>
                            <IoIosClose className={"cancel-categories"} onClick={() => handleRemove(index)} />
                        </div>
                    )}

                    {data.type === "video" && (
                        <div className={"preview-image-video"}>
                            <video src={data.file} controls className={"preview-item"} />
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
