import { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

interface DownloadFile {
  id: string;
  url: string;
  fileName: string;
  createdAt: string;
  status: string;
  filePath: string;
  format: string;
  totalBytes: number;
  downloadedBytes: number;
  progress: number;
}

function App() {

  const [url, setUrl] = useState("");
  const [type, setType] = useState("MP4");

  const [downloadId, setDownloadId] = useState<string | null>(null);
  const [download, setDownload] = useState<DownloadFile | null>(null);

  const [loading, setLoading] = useState(false);

  const downloadVideo = async () => {

    console.log("Posílám request na Spring...");

    setLoading(true);

    try {

      const response = await fetch(
        `http://localhost:8080/api/download?url=${encodeURIComponent(url)}&type=${type}`,
        {
          method: "POST"
        }
      );

      console.log("Spring odpověděl:", response.status);

      if (!response.ok) {
        throw new Error("Spring vrátil chybu: " + response.status);
      }

      const data = await response.json();

      console.log("Data ze Springu:", data);

      setDownloadId(data);

    } catch (error) {

      console.error("Chyba:", error);

    } finally {

      setLoading(false);

    }
  };


useEffect(() => {
  if (!downloadId) {
    return;
  }

  let cancelled = false;

  const checkStatus = async () => {
    if (cancelled) {
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/download/${downloadId}`
      );

      if (!response.ok) {
        throw new Error(
          "Nepodařilo se získat stav downloadu"
        );
      }

      const data: DownloadFile = await response.json();

      console.log("Download status:", data);

      if (cancelled) {
        return;
      }

      setDownload(data);

      // Download skončil → už nic dalšího neposílat
      if (
        data.status === "COMPLETED" ||
        data.status === "FAILED" ||
        data.status === "CANCELED"
      ) {
        console.log(
          "Download skončil:",
          data.status
        );

        return;
      }

      // Počkáme 1 sekundu a teprve potom pošleme další request
      setTimeout(checkStatus, 1000);

    } catch (error) {
      if (!cancelled) {
        console.error(
          "Chyba při získávání statusu:",
          error
        );

        // Při chybě také zkusíme znovu za 2 sekundy
        setTimeout(checkStatus, 2000);
      }
    }
  };

  checkStatus();

  return () => {
    cancelled = true;
  };

}, [downloadId]);


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
                  className="form-control"
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

                  <option value="MP4">
                    MP4
                  </option>

                  <option value="MP3">
                    MP3
                  </option>

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
                disabled={loading}
              >

                {loading
                  ? "Spouštím..."
                  : "Download"
                }

              </button>


              {download && (

                <div className="mt-4">

                  <h5>
                    {download.fileName}
                  </h5>

                  <p>
                    Status: <strong>{download.status}</strong>
                  </p>


                  <div className="progress">

                    <div
                      className="progress-bar"
                      role="progressbar"
                      style={{
                        width: `${download.progress}%`
                      }}
                    >
                      {download.progress.toFixed(1)}%
                    </div>

                  </div>


                  <div className="mt-2 text-muted">

                    {download.downloadedBytes.toLocaleString()}
                    {" / "}
                    {download.totalBytes.toLocaleString()}
                    {" bytes"}

                  </div>

                </div>

              )}

            </div>

          </div>

        </div>

      </div>

    </div>
  );
}

export default App;