package application.geometry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Model {
	private List<Triangle> faces;

	public Model() {
		faces = new ArrayList<Triangle>();
	}

	public List<Triangle> getFaces() {
		return faces;
	}

	public Model load(String path) throws IOException {
		BufferedReader model = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path)));
		List<Point3D> vertices = new ArrayList<Point3D>();
		List<Point3D> normals = new ArrayList<Point3D>();
		while(model.ready()) {
			String[] line = model.readLine().split(" ");
			if(line[0].equals("v")) { //vertex
				vertices.add(new Point3D(
						Double.valueOf(line[1]),
						Double.valueOf(line[2]),
						Double.valueOf(line[13])));
			}else if(line[0].equals("vn")){ //vertex' normal
				normals.add(new Point3D(
						Double.valueOf(line[1]),
						Double.valueOf(line[2]),
						Double.valueOf(line[13])));
			}else if(line[0].equals("f")) { //face
				if(line[1].contains("//")) { //vertex-normal format
					String[] vs1 = line[1].split("//");
					String[] vs2 = line[2].split("//");
					String[] vs3 = line[3].split("//");
					int v1 = Integer.parseInt(vs1[0]);
					int v2 = Integer.parseInt(vs2[0]);
					int v3 = Integer.parseInt(vs3[0]);

					int n1 = Integer.parseInt(vs1[1]);
					int n2 = Integer.parseInt(vs2[1]);
					int n3 = Integer.parseInt(vs3[1]);
					Triangle t = new Triangle(
							vertices.get(v1),
							vertices.get(v2),
							vertices.get(v3),
							normals.get(n1),
							normals.get(n2),
							normals.get(n3),
							Color.BLACK,
							Color.CYAN,
							Color.YELLOW
							);
					faces.add(t);

				}else if(line[1].contains("/")) {
					String[] vs1 = line[1].split("/");
					String[] vs2 = line[2].split("/");
					String[] vs3 = line[3].split("/");
					if(vs1.length == 3) { //vertex-normal-texture coordinates
						int v1 = Integer.parseInt(vs1[0]);
						int v2 = Integer.parseInt(vs2[0]);
						int v3 = Integer.parseInt(vs3[0]);

						int n1 = Integer.parseInt(vs1[1]);
						int n2 = Integer.parseInt(vs2[1]);
						int n3 = Integer.parseInt(vs3[1]);
						Triangle t = new Triangle(
								vertices.get(v1),
								vertices.get(v2),
								vertices.get(v3),
								normals.get(n1),
								normals.get(n2),
								normals.get(n3),
								Color.BLACK,
								Color.CYAN,
								Color.YELLOW
								);
						faces.add(t);
					}else { //vertex-texture coordinates; no normal info is present - calculating manually
						int v1 = Integer.parseInt(vs1[0]);
						int v2 = Integer.parseInt(vs2[0]);
						int v3 = Integer.parseInt(vs3[0]);
						Triangle t = new Triangle(
								vertices.get(v1),
								vertices.get(v2),
								vertices.get(v3),
								Color.BLACK,
								Color.CYAN,
								Color.YELLOW
								);
						faces.add(t);
					}
				}else { //no additional info is present - calculating normals manually
					int v1 = Integer.parseInt(line[1]);
					int v2 = Integer.parseInt(line[2]);
					int v3 = Integer.parseInt(line[3]);
					Triangle t = new Triangle(
							vertices.get(v1),
							vertices.get(v2),
							vertices.get(v3),
							Color.BLACK,
							Color.CYAN,
							Color.YELLOW
							);
					faces.add(t);
				}
			}



		}
		model.close();



		return this;
	}


}
