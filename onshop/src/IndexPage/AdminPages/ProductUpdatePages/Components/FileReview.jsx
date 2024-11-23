
// eslint-disable-next-line react/prop-types
const FileReview = ({previewImages}) => {

    return (
        <div className={"preview-holder"}>
            {/* eslint-disable-next-line react/prop-types */}
            {previewImages.length > 0 && previewImages.map(({file, type}, index) => (
                <div key={index}>
                    {type === "image" && (
                        <img src={file} alt="Preview" className={"preview-item"} />
                    )}

                    {type === "video" && (
                        <video src={file} controls className={"preview-item"} />
                    )}
                </div>
            ))
            }
        </div>
    );
};

export default FileReview;
