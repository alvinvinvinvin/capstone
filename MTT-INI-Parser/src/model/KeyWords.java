/**
 * 
 */
package model;

/**
 * @author alvin
 *
 */
public enum KeyWords {
	INCLUDE_SECTION {
		@Override
		public String toString() {
			return "include_section";
		}
	},
	EXEC_KEY {
		@Override
		public String toString() {
			return "exec";
		}
	},
	ALL_PHASENAME {
		@Override
		public String toString() {
			return "all";
		}
	},
	MTT_PHASENAME {
		@Override
		public String toString() {
			return "MTT";
		}
	},
	MPI_DETAILS_PHASENAME {
		@Override
		public String toString() {
			return "MPI Details";
		}
	},
	MPI_DETAILS_KEY {
		@Override
		public String toString() {
			return "mpi_details";
		}
	},
	MPI_GET_PHASENAME {
		@Override
		public String toString() {
			return "MPI get";
		}
	},
	MPI_GET_KEY {
		@Override
		public String toString() {
			return "mpi_get";
		}
	},
	MPI_INSTALL_PHASENAME {
		@Override
		public String toString() {
			return "MPI install";
		}
	},
	TEST_GET_PHASENAME {
		@Override
		public String toString() {
			return "Test get";
		}
	},
	TEST_GET_KEY {
		@Override
		public String toString() {
			return "test_get";
		}
	},
	TEST_BUILD_PHASENAME {
		@Override
		public String toString() {
			return "Test build";
		}
	},
	TEST_BUILD_KEY {
		@Override
		public String toString() {
			return "test_build";
		}
	},
	TEST_RUN_PHASENAME {
		@Override
		public String toString() {
			return "Test run";
		}
	},
	ENUMERATE {
		@Override
		public String toString() {
			return "enumerate";
		}
	},
	DATABASE_URL {
		@Override
		public String toString() {
			return "http://flux.cs.uwlax.edu/mtt/api";
		}
	},
	TESTSUITES_URL {
		@Override
		public String toString() {
			return "/info/testsuite";
		}
	},
	RUNTIME_URL {
		@Override
		public String toString() {
			return "/info/runtime";
		}
	},	
	RUNTIME_ZERO {
		@Override
		public String toString() {
			return "0:00:00";
		}
	},
	RUNTIME_ONE_MIN {
		@Override
		public String toString() {
			return "0:01:00";
		}
	},
}
