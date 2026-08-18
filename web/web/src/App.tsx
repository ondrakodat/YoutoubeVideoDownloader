import { useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [url, setUrl] = useState("");
  const [type, setType] = useState("MP4");

  const downloadVideo = async () => {
    console.log("Posílám request na Spring...");

    try {
      const response = await fetch(
        `http://localhost:8080/api/download?url=${encodeURIComponent(url)}&type=${type}`,
        {
          method: "POST"
        }
      );

      console.log("Spring odpověděl:", response.status);

      const data = await response.json();

      console.log("Data ze Springu:", data);

    } catch (error) {
      console.error("Chyba:", error);
    }
  };

  return (
    <div className="container mt-5">
      
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">

          <div className="card shadow">
            <div className="card-body p-4">

              <h1 className="text-center mb-4">
                Download URL Video
              </h1>

              <div className="mb-3">
                <label className="form-label">
                  URL videa
                </label>

                <input
                  className = "form-control w-50"
                  type="text"
                  placeholder="https://youtube.com/..."
                  value={url}
                  onChange={(e) => setUrl(e.target.value)}
                />
              </div>

              <div className="mb-4">
                <label className="form-label">
                  Formát
                </label>

                <select
                  className="form-select"
                  value={type}
                  onChange={(e) => setType(e.target.value)}
                >
                  <option value="MP4">MP4</option>
                  <option value="MP3">MP3</option>
                  <option value="MP4_SUBTITLES">
                    MP4 + subtitles
                  </option>
                  <option value="MP4_SUBTITLES_AUTO">
                    MP4 + automatic subtitles
                  </option>
                </select>
              </div>

              <button
                className="btn btn-primary w-100"
                onClick={downloadVideo}
              >
                Download
              </button>

            </div>
          </div>

        </div>
      </div>
    </div>
  );
}

export default App;